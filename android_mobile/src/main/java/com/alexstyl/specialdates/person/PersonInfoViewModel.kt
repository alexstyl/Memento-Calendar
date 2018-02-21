package com.alexstyl.specialdates.person

import com.alexstyl.android.ViewVisibility
import java.net.URI

class PersonInfoViewModel(val displayName: String,
                          val ageAndStarSignlabel: String,
                          @ViewVisibility val AgeAndStarSignVisibility: Int,
                          val image: URI,
                          val isVisible: Boolean)
