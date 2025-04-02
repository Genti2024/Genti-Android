package kr.genti.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kr.genti.common.manager.PermissionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PermissionManagerTest {

    private val permission = Manifest.permission.CAMERA
    private lateinit var context: Activity
    private lateinit var dummyUri: Uri
    private lateinit var dummyIntent: Intent

    @BeforeEach
    fun setUp() {
        // given: Activity 컨텍스트 생성 및 기본 stubbing
        context = mockk(relaxed = true)
        every { context.applicationContext } returns context
        every { context.packageName } returns "kr.genti.android"

        // static 메서드 mocking
        mockkStatic(ContextCompat::class)
        mockkStatic(ActivityCompat::class)

        // 더미 Uri와 Intent 생성 (실제 Uri.fromParts 호출을 피하기 위함)
        dummyUri = mockk()
        dummyIntent = mockk() // 실제 Intent 생성자가 호출되지 않음
        every { dummyIntent.data } returns dummyUri

        // PermissionManager 객체를 모킹하여 private 함수 intentToSetting을 대체함
        mockkObject(PermissionManager)
        every { PermissionManager["intentToSetting"](any<Context>()) } returns dummyIntent
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `권한이 이미 허용된 경우 checkPermissionAndLaunch에서 onPermissionGranted가 호출되어야 한다`() {
        // given
        every {
            ContextCompat.checkSelfPermission(context, permission)
        } returns PackageManager.PERMISSION_GRANTED

        var grantedCalled = false
        var notGrantedCalled = false
        var alreadyDeniedCalled = false

        // when
        PermissionManager.checkPermissionAndLaunch(
            permission = permission,
            context = context,
            onPermissionGranted = { grantedCalled = true },
            onPermissionNotGranted = { notGrantedCalled = true },
            onPermissionAlreadyDenied = { alreadyDeniedCalled = true }
        )

        // then
        assertTrue(grantedCalled)
        assertFalse(notGrantedCalled)
        assertFalse(alreadyDeniedCalled)
    }

    @Test
    fun `권한이 거부되었으나 아직 거부한 적이 없으면 checkPermissionAndLaunch에서 onPermissionNotGranted가 호출되어야 한다`() {
        // given
        every {
            ContextCompat.checkSelfPermission(context, permission)
        } returns PackageManager.PERMISSION_DENIED
        every {
            ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
        } returns false

        var grantedCalled = false
        var notGrantedCalled = false
        var alreadyDeniedCalled = false

        // when
        PermissionManager.checkPermissionAndLaunch(
            permission = permission,
            context = context,
            onPermissionGranted = { grantedCalled = true },
            onPermissionNotGranted = { notGrantedCalled = true },
            onPermissionAlreadyDenied = { intent -> alreadyDeniedCalled = true }
        )

        // then
        assertFalse(grantedCalled)
        assertTrue(notGrantedCalled)
        assertFalse(alreadyDeniedCalled)
    }

    @Test
    fun `권한이 거부되었으며 이미 거부한 적이 있으면 checkPermissionAndLaunch에서 onPermissionAlreadyDenied가 호출되어야 한다`() {
        // given
        every {
            ContextCompat.checkSelfPermission(context, permission)
        } returns PackageManager.PERMISSION_DENIED
        every {
            ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
        } returns true

        var grantedCalled = false
        var notGrantedCalled = false
        var alreadyDeniedCalled = false
        var receivedIntent: Intent? = null

        // when
        PermissionManager.checkPermissionAndLaunch(
            permission = permission,
            context = context,
            onPermissionGranted = { grantedCalled = true },
            onPermissionNotGranted = { notGrantedCalled = true },
            onPermissionAlreadyDenied = { intent ->
                alreadyDeniedCalled = true
                receivedIntent = intent
            }
        )

        // then
        assertFalse(grantedCalled)
        assertFalse(notGrantedCalled)
        assertTrue(alreadyDeniedCalled)
        assertEquals(dummyUri, receivedIntent?.data)
    }
}