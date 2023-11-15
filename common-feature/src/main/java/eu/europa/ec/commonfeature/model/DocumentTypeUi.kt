/*
 *
 *  * Copyright (c) 2023 European Commission
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package eu.europa.ec.commonfeature.model

data class DocumentUi(
    val documentId: String,
    val documentType: DocumentTypeUi,
    val documentStatus: DocumentStatusUi,
    val documentImage: String,
    val documentItems: List<DocumentItemUi>
)

data class DocumentItemUi(
    val title: String,
    val value: String
)

enum class DocumentTypeUi(
    val title: String
) {
    DRIVING_LICENSE(title = "Driving License"),
    DIGITAL_ID(title = "Digital ID"),
    OTHER(title = "Other document")
}

fun String.toDocumentTypeUi(): DocumentTypeUi = when(this){
    "eu.europa.ec.eudiw.pid.1" -> DocumentTypeUi.DIGITAL_ID
    "org.iso.18013.5.1.mDL" -> DocumentTypeUi.DRIVING_LICENSE
    else -> DocumentTypeUi.OTHER
}

enum class DocumentStatusUi(
    val title: String
) {
    ACTIVE(title = "Active"),
    INACTIVE(title = "Inactive")
}