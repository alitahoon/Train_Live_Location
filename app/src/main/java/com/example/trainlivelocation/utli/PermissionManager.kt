package com.example.trainlivelocation.utli

import Permission
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

const val DIALOG_PERMISSION_TITLE = "Permission required"
const val DIALOG_PERMISSION_DEFAULT_MESSAGE = "Default message"
const val DIALOG_PERMISSION_BUTTON_POSITIVE = "OK"

class PermissionManager private constructor(private val fragment: WeakReference<Fragment>) {

    private val requiredPermissions = mutableListOf<Permission>()
    private var rationale: String? = null
    private var callback: (Boolean) -> Unit = {}
    private var detailedCallback: (Map<Permission, Boolean>) -> Unit = {}

    private val permissionCheck =
        fragment.get()
            ?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
                sendResultAndCleanUp(grantResults)
            }

    companion object {
        fun from(fragment: Fragment) = PermissionManager(WeakReference(fragment))
    }

    fun rationale(description: String): PermissionManager {
        rationale = description
        return this
    }

    fun request(vararg permission: Permission): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }

    fun checkPermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        handlePermissionRequest()
    }

    fun checkDetailedPermission(callback: (Map<Permission, Boolean>) -> Unit) {
        this.detailedCallback = callback
        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        fragment.get()?.let { fragment ->
            when {
                areAllPermissionsGranted(fragment) -> sendPositiveResult()
                shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
                else -> requestPermissions()
            }
        }
    }

    private fun displayRationale(fragment: Fragment) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle(DIALOG_PERMISSION_TITLE)
            .setMessage(rationale ?: DIALOG_PERMISSION_DEFAULT_MESSAGE)
            .setCancelable(false)
            .setPositiveButton(DIALOG_PERMISSION_BUTTON_POSITIVE) { _, _ ->
                requestPermissions()
            }
            .show()
    }

    private fun sendPositiveResult() {
        sendResultAndCleanUp(getPermissionList().associate { it to true })
    }

    private fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
        callback(grantResults.all { it.value })
        detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
        cleanUp()
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        rationale = null
        callback = {}
        detailedCallback = {}
    }

    private fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

    private fun areAllPermissionsGranted(fragment: Fragment) =
        requiredPermissions.all { it.isGranted(fragment) }

    private fun shouldShowPermissionRationale(fragment: Fragment) =
        requiredPermissions.any { it.requiresRationale(fragment) }

    private fun getPermissionList() =
        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

    private fun Permission.isGranted(fragment: Fragment) =
        permissions.all { hasPermission(fragment, it) }

    private fun Permission.requiresRationale(fragment: Fragment) =
        permissions.any { fragment.shouldShowRequestPermissionRationale(it) }

    private fun hasPermission(fragment: Fragment, permission: String) =
        ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
}