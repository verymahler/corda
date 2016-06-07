package com.r3corda.protocols

import com.r3corda.core.contracts.SignedTransaction
import com.r3corda.core.crypto.SecureHash
import com.r3corda.core.messaging.SingleMessageRecipient

/**
 * Given a set of tx hashes (IDs), either loads them from local disk or asks the remote peer to provide them.
 *
 * A malicious response in which the data provided by the remote peer does not hash to the requested hash results in
 * [FetchDataProtocol.DownloadedVsRequestedDataMismatch] being thrown. If the remote peer doesn't have an entry, it
 * results in a [FetchDataProtocol.HashNotFound] exception. Note that returned transactions are not inserted into
 * the database, because it's up to the caller to actually verify the transactions are valid.
 */
class FetchTransactionsProtocol(requests: Set<SecureHash>, otherSide: SingleMessageRecipient) :
        FetchDataProtocol<SignedTransaction, SignedTransaction>(requests, otherSide) {
    companion object {
        const val TOPIC = "platform.fetch.tx"
    }

    override fun load(txid: SecureHash): SignedTransaction? = serviceHub.storageService.validatedTransactions.getTransaction(txid)
    override val queryTopic: String = TOPIC
}