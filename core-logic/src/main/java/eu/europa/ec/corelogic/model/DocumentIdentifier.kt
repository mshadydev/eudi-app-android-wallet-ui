package eu.europa.ec.corelogic.model

import eu.europa.ec.eudi.iso18013.transfer.RequestDocument
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.IssuedDocument

typealias DocType = String

sealed interface DocumentIdentifier {
    val nameSpace: String
    val docType: DocType

    data object PID : DocumentIdentifier {
        override val nameSpace: String
            get() = "eu.europa.ec.eudi.pid.1"
        override val docType: DocType
            get() = "eu.europa.ec.eudi.pid.1"
    }

    data object MDL : DocumentIdentifier {
        override val nameSpace: String
            get() = "org.iso.18013.5.1"
        override val docType: DocType
            get() = "org.iso.18013.5.1.mDL"
    }

    data object SAMPLE : DocumentIdentifier {
        override val nameSpace: String
            get() = "load_sample_documents"
        override val docType: DocType
            get() = "load_sample_documents"
    }

    data object AGE : DocumentIdentifier {
        override val nameSpace: String
            get() = "eu.europa.ec.eudi.pseudonym.age_over_18.1"
        override val docType: DocType
            get() = "eu.europa.ec.eudi.pseudonym.age_over_18.1"
    }

    data object PHOTOID : DocumentIdentifier {
        override val nameSpace: String
            get() = "org.iso.23220.photoid.1"
        override val docType: DocType
            get() = "org.iso.23220.2.photoid.1"
    }

    // تعريف الشهادة الجامعية
    data object UNICERT : DocumentIdentifier {
        override val nameSpace: String
            get() = "eu.europa.ec.eudi.unicert.1"
        override val docType: DocType
            get() = "eu.europa.ec.eudi.2.unicert.1"
    }

    data class OTHER(
        override val nameSpace: String,
        override val docType: DocType,
    ) : DocumentIdentifier
}

fun DocumentIdentifier.isSupported(): Boolean {
    return when (this) {
        is DocumentIdentifier.PID,
        DocumentIdentifier.MDL,
        DocumentIdentifier.AGE,
        DocumentIdentifier.PHOTOID,
        DocumentIdentifier.SAMPLE,
        DocumentIdentifier.UNICERT -> true
        is DocumentIdentifier.OTHER -> true
    }
}

/**
 * @return A [DocumentIdentifier] from a DocType.
 * This function should ONLY be called on docType and NOT on nameSpace.
 */
fun DocType.toDocumentIdentifier(): DocumentIdentifier = when (this) {
    DocumentIdentifier.PID.docType -> DocumentIdentifier.PID
    DocumentIdentifier.MDL.docType -> DocumentIdentifier.MDL
    DocumentIdentifier.SAMPLE.docType -> DocumentIdentifier.SAMPLE
    DocumentIdentifier.AGE.docType -> DocumentIdentifier.AGE
    DocumentIdentifier.PHOTOID.docType -> DocumentIdentifier.PHOTOID
    DocumentIdentifier.UNICERT.docType -> DocumentIdentifier.UNICERT  // دعم الشهادة الجامعية
    else -> DocumentIdentifier.OTHER(
        nameSpace = this,
        docType = this
    )
}

fun Document.toDocumentIdentifier(): DocumentIdentifier {
    val nameSpace = (this as? IssuedDocument)?.nameSpaces?.keys?.firstOrNull().orEmpty()
    val docType = this.docType

    return createDocumentIdentifier(nameSpace, docType)
}

fun RequestDocument.toDocumentIdentifier(): DocumentIdentifier {
    val nameSpace = this.docRequest.requestItems.firstOrNull()?.namespace.orEmpty()
    val docType = this.docType

    return createDocumentIdentifier(nameSpace, docType)
}

private fun createDocumentIdentifier(nameSpace: String, docType: DocType): DocumentIdentifier {
    return when {
        nameSpace == DocumentIdentifier.PID.nameSpace
                && docType == DocumentIdentifier.PID.docType -> DocumentIdentifier.PID

        nameSpace == DocumentIdentifier.MDL.nameSpace
                && docType == DocumentIdentifier.MDL.docType -> DocumentIdentifier.MDL

        nameSpace == DocumentIdentifier.SAMPLE.nameSpace
                && docType == DocumentIdentifier.SAMPLE.docType -> DocumentIdentifier.SAMPLE

        nameSpace == DocumentIdentifier.AGE.nameSpace
                && docType == DocumentIdentifier.AGE.docType -> DocumentIdentifier.AGE

        nameSpace == DocumentIdentifier.PHOTOID.nameSpace
                && docType == DocumentIdentifier.PHOTOID.docType -> DocumentIdentifier.PHOTOID

        nameSpace == DocumentIdentifier.UNICERT.nameSpace
                && docType == DocumentIdentifier.UNICERT.docType -> DocumentIdentifier.UNICERT  // دعم الشهادة الجامعية

        else -> DocumentIdentifier.OTHER(
            nameSpace = nameSpace,
            docType = docType
        )
    }
}
