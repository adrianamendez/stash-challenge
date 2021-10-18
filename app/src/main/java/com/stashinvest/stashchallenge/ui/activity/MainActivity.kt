package com.stashinvest.stashchallenge.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stashinvest.stashchallenge.App
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.ui.fragment.MainFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.internal.operators.maybe.MaybeToPublisher.instance

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MainFragment.newInstance())
                .commit()
        }
    }
}
