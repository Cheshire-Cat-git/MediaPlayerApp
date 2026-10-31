package com.example.mediaplayerapp

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mediaplayerapp.data.TokenManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowUiTest {

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val tokenManager = TokenManager(context)
        tokenManager.clearToken()
    }

    @Test
    fun loginSuccess() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.etUsername))
                .perform(replaceText("validUser"), closeSoftKeyboard())

            onView(withId(R.id.etPassword))
                .perform(replaceText("validPassword"), closeSoftKeyboard())

            onView(withId(R.id.btnLogin))
                .perform(click())

            onView(withId(R.id.btn_Audio))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun loginFailureShowsError() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.etUsername))
                .perform(replaceText(""), closeSoftKeyboard())

            onView(withId(R.id.btnLogin))
                .perform(click())

            onView(withId(R.id.tvError))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun autoRedirectValidToken() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val tokenManager = TokenManager(context)
        tokenManager.saveToken("valid_test_token", System.currentTimeMillis())

        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_Audio))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun expiredTokenShowsLogin() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val tokenManager = TokenManager(context)
        val expiredTime = System.currentTimeMillis() - (11 * 60 * 1000L)
        tokenManager.saveToken("expired_test_token", expiredTime)

        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btnLogin))
                .check(matches(isDisplayed()))
        }
    }
}