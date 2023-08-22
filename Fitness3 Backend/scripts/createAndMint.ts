import { Keypair, LAMPORTS_PER_SOL, clusterApiUrl } from "@solana/web3.js";
import { loadWalletKey, sendVersionedTx } from "./utils";
import { ValidDepthSizePair } from "@solana/spl-account-compression";
import {
  MetadataArgs,
  TokenProgramVersion,
  TokenStandard,
} from "@metaplex-foundation/mpl-bubblegum";
import { CreateMetadataAccountArgsV3 } from "@metaplex-foundation/mpl-token-metadata";

// import custom helpers for demos
import { loadKeypairFromFile, loadOrGenerateKeypair, numberFormatter } from "@/utils/helpers";

// import custom helpers to mint compressed NFTs
import { createCollection, createTree, mintCompressedNFT } from "@/utils/compression";

// local import of the connection wrapper, to help with using the ReadApi
import { WrapperConnection } from "@/ReadApi/WrapperConnection";

import dotenv from "dotenv";
dotenv.config();

// define some reusable balance values for tracking
let initBalance: number, balance: number;

(async () => {
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  // generate a new Keypair for testing, named `wallet`
  const testWallet = loadOrGenerateKeypair("testWallet");
  
  // generate a new keypair for use in this demo (or load it locally from the filesystem when available)
  const payer = process.env?.LOCAL_PAYER_JSON_ABSPATH
    ? loadKeypairFromFile(process.env?.LOCAL_PAYER_JSON_ABSPATH)
    : loadOrGenerateKeypair("payer");

  console.log("Payer address:", payer.publicKey.toBase58());
  console.log("Payer key 1:", payer.secretKey);
  console.log("Test wallet address:", testWallet.publicKey.toBase58());

  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  // load the env variables and store the cluster RPC url
  const CLUSTER_URL = process.env.RPC_URL ?? clusterApiUrl("devnet");

  // create a new rpc connection, using the ReadApi wrapper
  const connection = new WrapperConnection(CLUSTER_URL, "confirmed");

  // get the payer's starting balance (only used for demonstration purposes)
  initBalance = await connection.getBalance(payer.publicKey);

  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  /*
    Define our tree size parameters
  */
  const maxDepthSizePair: ValidDepthSizePair = {
    // max=16,384 nodes
    maxDepth: 14,
    maxBufferSize: 64,
  };
  const canopyDepth = maxDepthSizePair.maxDepth - 5;

  /*
    Actually allocate the tree on chain
  */

  // define the address the tree will live at
  const treeKeypair = Keypair.generate();

  // create and send the transaction to create the tree on chain
  const tree = await createTree(connection, payer, treeKeypair, maxDepthSizePair, canopyDepth);

  /*
    Create the actual NFT collection (using the normal Metaplex method)
    (nothing special about compression here)
  */

  // define the metadata to be used for creating the NFT collection
  const collectionMetadataV3: CreateMetadataAccountArgsV3 = {
    data: {
      name: "Fitness3",
      symbol: "F3",
      // specific json metadata for the collection
      uri: "https://s3an576r6lezwxeejva4qjnj4xyioph3cwhelobtwkbzejn3qk5q.arweave.net/lsDe_9HyyZtchE1ByCWp5fCHPPsVjkW4M7KDkiW7grs",
      sellerFeeBasisPoints: 100,
      creators: [
        {
          address: payer.publicKey,
          verified: false,
          share: 100,
        },
      ],
      collection: null,
      uses: null,
    },
    isMutable: false,
    collectionDetails: null,
  };

  // create a full token mint and initialize the collection (with the `payer` as the authority)
  const collection = await createCollection(connection, payer, collectionMetadataV3);

  /*
    Mint a single compressed NFT
  */

  const compressedNFTMetadata: MetadataArgs = {
    name: "Award NFT",
    symbol: collectionMetadataV3.data.symbol,
    // specific json metadata for each NFT
    uri: "https://s3an576r6lezwxeejva4qjnj4xyioph3cwhelobtwkbzejn3qk5q.arweave.net/lsDe_9HyyZtchE1ByCWp5fCHPPsVjkW4M7KDkiW7grs",
    creators: [
      {
        address: payer.publicKey,
        verified: false,
        share: 100,
      },
      {
        address: testWallet.publicKey,
        verified: false,
        share: 0,
      },
    ],
    editionNonce: 0,
    uses: null,
    collection: null,
    primarySaleHappened: false,
    sellerFeeBasisPoints: 0,
    isMutable: false,
    // these values are taken from the Bubblegum package
    tokenProgramVersion: TokenProgramVersion.Original,
    tokenStandard: TokenStandard.NonFungible,
  };

  // fully mint a single compressed NFT to the payer
  console.log(`Minting a single compressed NFT to ${payer.publicKey.toBase58()}...`);

  await mintCompressedNFT(
    connection,
    payer,
    treeKeypair.publicKey,
    collection.mint,
    collection.metadataAccount,
    collection.masterEditionAccount,
    compressedNFTMetadata,
    // mint to this specific wallet (in this case, the tree owner aka `payer`)
    payer.publicKey,
  );

  // fully mint a single compressed NFT
  console.log(`Minting a single compressed NFT to ${testWallet.publicKey.toBase58()}...`);
  const user = loadWalletKey("mykey.json")
  await mintCompressedNFT(
    connection,
    payer,
    treeKeypair.publicKey,
    collection.mint,
    collection.metadataAccount,
    collection.masterEditionAccount,
    compressedNFTMetadata,
    // mint to this specific wallet (in this case, airdrop to `testWallet`)
    user.publicKey,
  );

  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  // fetch the payer's final balance
  balance = await connection.getBalance(payer.publicKey);

  console.log(`===============================`);
  console.log(
    "Total cost:",
    numberFormatter((initBalance - balance) / LAMPORTS_PER_SOL, true),
    "SOL\n",
  );
})();
