package io.github.diegoflassa.template_android_app.helper

import io.github.diegoflassa.template_android_app.data.entities.Patient
import io.github.diegoflassa.template_android_app.data.entities.Patients

class SearchResult {
    lateinit var patients : Patients
    var lastSearchedPage = 0
}
