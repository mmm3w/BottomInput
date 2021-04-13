package com.mitsuki.bottominput.custom.helper

import androidx.fragment.app.Fragment

class FragmentHelper(count: Int, val fragmentProvider: (Int) -> Fragment) {
    private val fragments: Array<Fragment?> = Array(count) { null }

    fun obtainFragment(index: Int): Fragment {
        return fragments[index] ?: fragmentProvider(index).apply { fragments[index] = this }
    }
}