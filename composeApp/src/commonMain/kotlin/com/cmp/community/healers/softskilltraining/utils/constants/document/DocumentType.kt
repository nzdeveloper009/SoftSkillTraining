package com.cmp.community.healers.softskilltraining.utils.constants.document

enum class DocumentType(
    val label: String,
    val format: String,
    val acceptPdf: Boolean,
    val acceptImage: Boolean
) {
    CANDIDATE_PHOTO("Candidate Photo",              "Format: Image",       false, true),
    CNIC_FRONT(     "CNIC Front",                   "Format: Image/PDF",   true,  true),
    CNIC_BACK(      "CNIC Back",                    "Format: Image/PDF",   true,  true),
    POLICE_CERT(    "Police Clearance Certificate", "Format: PDF",         true,  false),
    MEDICAL_CERT(   "Medical Certificate",          "Format: PDF",         true,  false),
    PASSPORT(       "Passport",                     "Format: PDF",         true,  false),
}