package com.example.mediaplayerapp

import android.content.Context
import android.content.SharedPreferences
import com.example.mediaplayerapp.data.TokenManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.nullable
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class TokenManagerTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var tokenManager: TokenManager

    private val prefsMap = mutableMapOf<String, Any>()

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenAnswer { invocation ->
            prefsMap[invocation.getArgument(0)] = invocation.getArgument(1)
            editor
        }
        `when`(editor.putLong(anyString(), anyLong())).thenAnswer { invocation ->
            prefsMap[invocation.getArgument(0)] = invocation.getArgument(1)
            editor
        }
        `when`(editor.clear()).thenAnswer {
            prefsMap.clear()
            editor
        }
        `when`(sharedPreferences.getString(anyString(), nullable(String::class.java))).thenAnswer { invocation ->
            prefsMap[invocation.getArgument(0)] as? String ?: invocation.getArgument(1)
        }
        `when`(sharedPreferences.getLong(anyString(), anyLong())).thenAnswer { invocation ->
            prefsMap[invocation.getArgument(0)] as? Long ?: invocation.getArgument(1)
        }

        tokenManager = TokenManager(context)
    }

    @Test
    fun noTokenInvalid() {
        assertFalse(tokenManager.isTokenValid())
    }

    @Test
    fun validWithinTenMinutes() {
        val now = 1000000L
        tokenManager.saveToken("valid_token", now)

        val fiveMinutesLater = now + (5 * 60 * 1000L)
        assertTrue(tokenManager.isTokenValid(fiveMinutesLater))
    }

    @Test
    fun invalidAfterTenMinutes() {
        val now = 1000000L
        tokenManager.saveToken("valid_token", now)

        val elevenMinutesLater = now + (11 * 60 * 1000L)
        assertFalse(tokenManager.isTokenValid(elevenMinutesLater))
    }

    @Test
    fun clearToken() {
        tokenManager.saveToken("valid_token", System.currentTimeMillis())
        tokenManager.clearToken()
        assertFalse(tokenManager.isTokenValid())
    }
}