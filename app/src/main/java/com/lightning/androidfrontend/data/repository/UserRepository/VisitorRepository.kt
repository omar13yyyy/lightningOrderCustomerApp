package com.lightning.androidfrontend.data.repository.UserRepository

import NetworkClient
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lightning.androidfrontend.data.api.VisitorApi
import com.lightning.androidfrontend.data.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VisitorRepository : VisitorApi {

    val customer: String = "customer/"
    val order: String = "order/"


    override suspend fun login(userLoginParams: UserLoginParams): UserLoginRes? {

        val type = object : TypeToken<UserLoginRes>() {}.type
        val myRes: UserLoginRes? = NetworkClient.makeRequest(
            "POST", bodyData = userLoginParams,
            url = customer + "login", responseType = type
        )
        Log.d("debugTag", "myRes is: $myRes")
        return myRes;
    }


    override suspend fun confirmation(phoneNumber: String): PostVirtualRes? {
        val param = PhoneNumberClass(phoneNumber)
        val type = object : TypeToken<PostVirtualRes>() {}.type
        val myRes: PostVirtualRes? = NetworkClient.makeRequest(
            "POST", bodyData = param,
            url = customer + "confirmation", responseType = type
        )
        return myRes;
    }

    override suspend fun restConfirmation(phoneNumber: String): PostVirtualRes? {
        val param = PhoneNumberClass(phoneNumber)
        val type = object : TypeToken<PostVirtualRes>() {}.type
        val myRes: PostVirtualRes? = NetworkClient.makeRequest(
            "POST", bodyData = param,
            url = customer + "restConfirmation", responseType = type
        )
        return myRes;
    }

    override suspend fun checkCodeValidity(checkCodeParams: CheckCodeParams): PostVirtualRes? {
        val type = object : TypeToken<PostVirtualRes>() {}.type
        val myRes: PostVirtualRes? = NetworkClient.makeRequest(
            "POST", bodyData = checkCodeParams,
            url = customer + "checkCodeValidity", responseType = type
        )
        return myRes;
    }

    override suspend fun register(userRegParams: UserRegParams): RegisterRes? {
        val type = object : TypeToken<RegisterRes>() {}.type
        val myRes: RegisterRes? = NetworkClient.makeRequest(
            "POST", bodyData = userRegParams,
            url = customer + "register", responseType = type
        )
        return myRes;
    }

    override suspend fun restPassword(restPasswordParams: RestPasswordParams): PostVirtualRes? {
        val type = object : TypeToken<PostVirtualRes>() {}.type
        val myRes: PostVirtualRes? = NetworkClient.makeRequest(
            "POST", bodyData = restPasswordParams,
            url = customer + "restPassword", responseType = type
        )
        return myRes;
    }




    override suspend fun getStoreCategories(getStoreCategoriesParams: GetStoreCategoriesParams): List<GetStoreCategoriesRes>? {

        val type = object : TypeToken<List<GetStoreCategoriesRes>>() {}.type
        val myRes: List<GetStoreCategoriesRes>? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = customer + "getStoreCategories?ln=${getStoreCategoriesParams.ln}", responseType = type,
        )
        return myRes;


    }

    override suspend fun getCategoryTags(getCategoryTagsParams: GetCategoryTagsParams): List<GetCategoryTagsRes>? {

        val type = object : TypeToken<List<GetCategoryTagsRes>>() {}.type
        val myRes: List<GetCategoryTagsRes>? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = customer + "getCategoryTags?ln=${getCategoryTagsParams.ln}&categoryId=${getCategoryTagsParams.categoryId}", responseType = type,
        )
        return myRes;
    }


    override suspend fun getNearStores(getNearStoresParams: GetNearStoresParams): GetStoresRes? {
    val type = object : TypeToken<GetStoresRes>() {}.type
    val myRes: GetStoresRes? = NetworkClient.makeRequest(
        "POST", bodyData = getNearStoresParams,
        url = customer + "getNearStores", responseType = type,
    )
    return myRes;


}

override suspend fun getStoresByCategory(getNearStoresByCategoryParams: GetNearStoresByCategoryParams): GetStoresRes? {
    val type = object : TypeToken<GetStoresRes>() {}.type
    val myRes: GetStoresRes? = NetworkClient.makeRequest(
        "POST", bodyData = getNearStoresByCategoryParams,
        url = customer + "getStoresByCategory", responseType = type,
    )
    return myRes;


}

override suspend fun getStoresByTag(
    getStoreCategories: GetNearStoresByTagParams,
): GetStoresRes? {
    val type = object : TypeToken<GetStoresRes>() {}.type
    val myRes: GetStoresRes? = NetworkClient.makeRequest(
        "POST", bodyData = getStoreCategories,
        url = customer + "getStoresByTag", responseType = type,
    )
    return myRes;

}

    override suspend fun getStoreProducts(getStoreProductsParams: GetStoreProductsParams): GetStoreProductsRes? {
        val type = object : TypeToken<GetStoreProductsRes>() {}.type
        val myRes: GetStoreProductsRes? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = customer + "getStoreProducts?storeId=${getStoreProductsParams.storeId}&ln=${getStoreProductsParams.ln}", responseType = type,
        )
        return myRes;    }

    override suspend fun searchForStore(searchForStoresParams: SearchForStoresParams): GetStoresRes? {
        val type = object : TypeToken<GetStoresRes>() {}.type
        val myRes: GetStoresRes? = NetworkClient.makeRequest(
            "POST", bodyData = searchForStoresParams,
            url = customer + "SearchForStore", responseType = type,
        )
        return myRes;


    }

    override suspend fun getWorkShifts(getWorkShiftsParams: GetWorkShiftsParams): GetWorkShiftsRes? {
        val type = object : TypeToken<GetWorkShiftsRes>() {}.type
        val myRes: GetWorkShiftsRes? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = customer + "getWorkShifts?storeId=${getWorkShiftsParams.storeId}", responseType = type,
        )
        return myRes;
    }

    override suspend fun getCouponDetails(getCouponDetailsParams: GetCouponDetailsParams): GetCouponDetailsRes? {
        val type = object : TypeToken<GetCouponDetailsRes>() {}.type
        val myRes: GetCouponDetailsRes? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = customer + "getWorkShifts?couponCode=${getCouponDetailsParams.couponCode}", responseType = type,
        )
        return myRes;

    }

    override suspend fun sendUserOrder(
        sendUserOrderParams: SendUserOrderParams,
        token: String
    ): PostVirtualRes? {
        val type = object : TypeToken<PostVirtualRes>() {}.type
        val myRes: PostVirtualRes? = NetworkClient.makeRequest(
            "POST", bodyData = sendUserOrderParams,
            url = order + "sendUserOrder", responseType = type
        )
        return myRes;
    }

    override suspend fun currentCustomerOrder(limitDateOffsetParams :LimitDateOffset,token: String): UserOrdersRes? {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Bearer $token"
        val type = object : TypeToken<UserOrdersRes>() {}.type
        val myRes: UserOrdersRes? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = order + "currentCustomerOrder?limit=${limitDateOffsetParams.limit}&dateOffset=${limitDateOffsetParams.offset}", responseType = type, headers = headers
        )
        return myRes;
    }

    override suspend fun previousCustomerOrder(limitDateOffsetParams :LimitDateOffset,token: String): UserOrdersRes? {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Bearer $token"
        val type = object : TypeToken<UserOrdersRes>() {}.type
        val myRes: UserOrdersRes? = NetworkClient.makeRequest(
            "GET", bodyData = null,
            url = order + "previousCustomerOrder?limit=${limitDateOffsetParams.limit}&dateOffset=${limitDateOffsetParams.offset}", responseType = type, headers = headers
        )
        return myRes;
    }


}