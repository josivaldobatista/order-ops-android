package com.jfb.orderops.core.storage

import android.content.Context
import com.jfb.orderops.auth.domain.model.AuthSession

class SessionStorage(
    context: Context
) {

    private val preferences = context.getSharedPreferences(
        "order_ops_session",
        Context.MODE_PRIVATE
    )

    fun save(session: AuthSession) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, session.accessToken)
            .putString(KEY_TOKEN_TYPE, session.tokenType)
            .putLong(KEY_USER_ID, session.userId)
            .putLong(KEY_COMPANY_ID, session.companyId)
            .putString(KEY_NAME, session.name)
            .putString(KEY_EMAIL, session.email)
            .putString(KEY_ROLE, session.role)
            .apply()
    }

    fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getTokenType(): String {
        return preferences.getString(KEY_TOKEN_TYPE, "Bearer") ?: "Bearer"
    }

    fun isLoggedIn(): Boolean {
        return !getAccessToken().isNullOrBlank()
    }

    fun getCompanyId(): Long {
        return preferences.getLong(KEY_COMPANY_ID, 0L)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_TOKEN_TYPE = "token_type"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_COMPANY_ID = "company_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE = "role"
    }
}