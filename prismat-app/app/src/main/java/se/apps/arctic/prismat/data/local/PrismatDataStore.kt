package se.apps.arctic.prismat.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import se.apps.arctic.prismat.domain.model.Discount

val Context.dataStore by preferencesDataStore(name = "prismat_preferences")

class PrismatDataStore(private val context: Context) {
    private val gson = Gson()

    companion object {
        private val SELECTED_FILTER_KEY = stringPreferencesKey("selected_filter")

        private fun storeUrlKey(storeName: String) = stringPreferencesKey("store_url_$storeName")
        private fun selectedDiscountsKey(storeName: String, storeUrl: String) = stringPreferencesKey("selected_discounts_${storeName}_$storeUrl")
        private fun discountListKey(storeUrl: String) = stringPreferencesKey("discount_list_$storeUrl")
    }

    val selectedFilterFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SELECTED_FILTER_KEY] ?: "ALL"
    }

    suspend fun saveSelectedFilter(filter: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_FILTER_KEY] = filter
        }
    }

    suspend fun getSelectedFilter(): String {
        return selectedFilterFlow.first()
    }

    // 1. storeItem.url for each store & handling weekly update cleanup
    suspend fun saveStoreUrl(storeName: String, url: String) {
        context.dataStore.edit { preferences ->
            val key = storeUrlKey(storeName)
            val oldUrl = preferences[key]
            if (oldUrl != null && oldUrl != url) {
                // If storeItem.url doesn't match a new one, delete information in that storeItem.url
                preferences.remove(selectedDiscountsKey(storeName, oldUrl))
                preferences.remove(discountListKey(oldUrl))
            }
            preferences[key] = url
        }
    }

    suspend fun getStoreUrl(storeName: String): String? {
        return context.dataStore.data.first()[storeUrlKey(storeName)]
    }

    // 3. selectedDiscountIds based on a) id, b) store, c) storeItem.url
    suspend fun saveSelectedDiscountIds(storeName: String, storeUrl: String, ids: Set<Int>) {
        context.dataStore.edit { preferences ->
            val key = selectedDiscountsKey(storeName, storeUrl)
            preferences[key] = gson.toJson(ids)
        }
    }

    suspend fun getSelectedDiscountIds(storeName: String, storeUrl: String): Set<Int> {
        val json = context.dataStore.data.first()[selectedDiscountsKey(storeName, storeUrl)] ?: return emptySet()
        val type = object : TypeToken<Set<Int>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    // 4. list in fetchWillysDiscounts stored based on storeItem.url
    suspend fun saveDiscountsForUrl(storeUrl: String, discounts: List<Discount>) {
        context.dataStore.edit { preferences ->
            val key = discountListKey(storeUrl)
            preferences[key] = gson.toJson(discounts)
        }
    }

    suspend fun getDiscountsForUrl(storeUrl: String): List<Discount> {
        val json = context.dataStore.data.first()[discountListKey(storeUrl)] ?: return emptyList()
        val type = object : TypeToken<List<Discount>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
