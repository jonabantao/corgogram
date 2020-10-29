package com.abantaoj.corgogram.models

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post : ParseObject() {
    companion object {
        const val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        const val KEY_IMAGE = "KEY_IMAGE"
        const val KEY_USER = "KEY_USER"
        const val KEY_CREATED_AT = "createdAt"
    }

    var description: String?
        get() = getString(KEY_DESCRIPTION)
        set(description) {
            if (description != null) {
                put(KEY_DESCRIPTION, description)
            }
        }

    var image: ParseFile?
        get() = getParseFile(KEY_IMAGE)
        set(parseFile) {
            if (parseFile != null) {
                put(KEY_IMAGE, parseFile)
            }
        }

    var user: ParseUser?
        get() = getParseUser(KEY_USER)
        set(user) {
            if (user != null) {
                put(KEY_USER, user)
            }
        }
}