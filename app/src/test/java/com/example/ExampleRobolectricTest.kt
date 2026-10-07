package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ridhima.exe", appName)
  }

  @Test
  fun `verify daily mood check-in persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.example.data.AppPreferences(context)
    prefs.saveMood("ROMANTIC")
    assertEquals("ROMANTIC", prefs.getSelectedMood())
    assertEquals(true, prefs.isMoodCheckedInToday())
  }
}
