package com.lightning.androidfrontend.data.api

import com.lightning.androidfrontend.data.model.PostVirtualRes
import com.lightning.androidfrontend.data.model.UserLoginParams
import com.lightning.androidfrontend.data.model.UserLoginRes
import com.lightning.androidfrontend.data.model.UserRegParams
import com.lightning.androidfrontend.data.model.CheckCodeParams
import com.lightning.androidfrontend.data.model.*
import com.lightning.androidfrontend.data.model.GetStoreCategoriesParams
import com.lightning.androidfrontend.data.model.GetStoreCategoriesRes
import com.lightning.androidfrontend.data.model.RegisterRes
import com.lightning.androidfrontend.data.model.RestPasswordParams


interface VisitorApi {
    suspend fun login( userLoginParams: UserLoginParams): UserLoginRes?
    suspend fun confirmation( phoneNumber: String): PostVirtualRes?
    suspend fun restConfirmation( phoneNumber: String): PostVirtualRes?
    suspend fun checkCodeValidity( checkCodeParams: CheckCodeParams): PostVirtualRes?
    suspend fun register( userRegParams: UserRegParams): RegisterRes?
    suspend fun restPassword( restPasswordParams: RestPasswordParams): PostVirtualRes?

    suspend fun getStoreCategories( getStoreCategoriesParams: GetStoreCategoriesParams):  List<GetStoreCategoriesRes>?
    suspend fun getCategoryTags( getCategoryTagsParams: GetCategoryTagsParams):  List<GetCategoryTagsRes>?



    suspend fun getNearStores(getNearStoresParams: GetNearStoresParams):  GetStoresRes?
    suspend fun getStoresByCategory(getNearStoresByCategoryParams: GetNearStoresByCategoryParams):  GetStoresRes?
    suspend fun getStoresByTag( getStoreCategoriesParams: GetNearStoresByTagParams):  GetStoresRes?

    suspend fun getStoreProducts( getStoreProductsParams:  GetStoreProductsParams):  GetStoreProductsRes?

    suspend fun searchForStore(searchForStoresParams: SearchForStoresParams):  GetStoresRes?
    suspend fun getWorkShifts(getWorkShiftsParams: GetWorkShiftsParams):  GetWorkShiftsRes?
    suspend fun getCouponDetails( getCouponDetailsParams: GetCouponDetailsParams):  GetCouponDetailsRes?

    suspend fun sendUserOrder(sendUserOrderParams: SendUserOrderParams,token: String):  PostVirtualRes?




}