package se.apps.arctic.prismat.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
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
        private fun selectedDiscountsKey(storeName: String) = stringPreferencesKey("selected_discounts_${storeName}")
        private fun favoritesDiscountsKey(storeName: String) = stringPreferencesKey("favorite_discounts_${storeName}")
        private fun discountListKey(storeName: String) = stringPreferencesKey("discount_list_$storeName")
    }

    /*** LAST SELECTED FILTER ***/

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

    /*** STORE URL ***/

    suspend fun saveStoreUrl(storeName: String, url: String) {
        context.dataStore.edit { preferences ->
            val key = storeUrlKey(storeName)
            val oldUrl = preferences[key]

            if (oldUrl != null && oldUrl != url) {
                deleteStoreInformation(storeName, preferences)
            }

            preferences[key] = url
        }
    }

    fun deleteStoreInformation(storeName: String, preferences: MutablePreferences) {
        preferences.remove(selectedDiscountsKey(storeName))
        preferences.remove(favoritesDiscountsKey(storeName))
        preferences.remove(discountListKey(storeName))
    }

    /*** ID HANDLER HELPER ***/

    suspend fun getIds(key: Preferences.Key<String>): Set<Int> {
        val json = context.dataStore.data.first()[key] ?: return emptySet()
        val type = object : TypeToken<Set<Int>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    suspend fun saveIds(key: Preferences.Key<String>, ids: Set<Int>) {
        context.dataStore.edit { preferences ->
            preferences[key] = gson.toJson(ids)
        }
    }

    /*** DISCOUNT IDS ***/

    suspend fun getSelectedDiscountIds(storeName: String): Set<Int> {
        return getIds(selectedDiscountsKey(storeName))
    }

    suspend fun saveSelectedDiscountIds(storeName: String, ids: Set<Int>) {
        return saveIds(selectedDiscountsKey(storeName), ids)
    }

    /*** FAVORITES IDS ***/

    suspend fun getFavoriteDiscountIds(storeName: String): Set<Int> {
        return getIds(favoritesDiscountsKey(storeName))
    }

    suspend fun saveFavoriteDiscountIds(storeName: String, ids: Set<Int>) {
        return saveIds(favoritesDiscountsKey(storeName), ids)
    }

    /*** DISCOUNT LISTS FOR EACH STORE ***/

    suspend fun saveDiscountsForUrl(storeName: String, discounts: List<Discount>) {
        context.dataStore.edit { preferences ->
            val key = discountListKey(storeName)
            preferences[key] = gson.toJson(discounts)
        }
    }

    suspend fun getDiscountsForUrl(storeName: String): List<Discount> {
        val json = context.dataStore.data.first()[discountListKey(storeName)] ?: return emptyList()
        val type = object : TypeToken<List<Discount>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
