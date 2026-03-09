package com.cmp.community.healers.softskilltraining.utils.constants.document

enum class DocumentType(
    val label: String,
    val format: String,
    val acceptPdf: Boolean,
    val acceptImage: Boolean,
    val apiType: String
) {
    CANDIDATE_PHOTO("Candidate Photo",              "Format: Image",       false, true,  "photo"),
    CNIC_FRONT(     "CNIC Front",                   "Format: Image/PDF",   true,  true,  "cnicFront"),
    CNIC_BACK(      "CNIC Back",                    "Format: Image/PDF",   true,  true,  "cnicBack"),
    POLICE_CERT(    "Police Clearance Certificate", "Format: PDF",         true,  false, "policeClearance"),
    MEDICAL_CERT(   "Medical Certificate",          "Format: PDF",         true,  false, "medicalCertificate"),
    PASSPORT(       "Passport",                     "Format: PDF",         true,  false, "passport"),
}

fun documentTypeFromApiType(apiType: String): DocumentType? =
    DocumentType.entries.firstOrNull { it.apiType == apiType }