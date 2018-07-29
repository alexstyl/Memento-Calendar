package com.alexstyl.specialdates.person

import com.alexstyl.android.ViewVisibility
import com.alexstyl.specialdates.contact.ImageURL

class PersonInfoViewModel(val displayName: String,
                          val ageAndStarSignlabel: String,
                          @ViewVisibility val AgeAndStarSignVisibility: Int,
                          val image: ImageURL,
                          val isVisible: Boolean)
