package com.lightning.androidfrontend.ui.screens.home


import TokenManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lightning.androidfrontend.data.model.GetStoreCategoriesParams
import com.lightning.androidfrontend.data.model.GetStoreCategoriesRes
import com.lightning.androidfrontend.data.model.*
import com.lightning.androidfrontend.data.repository.UserRepository.VisitorRepository
import com.lightning.androidfrontend.utils.LanguagePreferences
import com.lightning.androidfrontend.utils.convertToMenuCategories
import com.lightning.androidfrontend.utils.encodeToQuadrants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class GetStoreCategoryState {
    object Idle : GetStoreCategoryState()
    object Loading : GetStoreCategoryState()
    data class Success(val message: String = "Login Successful") : GetStoreCategoryState()
    data class Error(val error: String) : GetStoreCategoryState()
}

sealed class GetNearStoresState {
    object Idle : GetNearStoresState()
    object Loading : GetNearStoresState()
    data class Success(val message: String = "Login Successful") : GetNearStoresState()
    data class Error(val error: String) : GetNearStoresState()
}

sealed class GetStoresByCategoryState {
    object Idle : GetStoresByCategoryState()
    object Loading : GetStoresByCategoryState()
    data class Success(val message: String = "Login Successful") : GetStoresByCategoryState()
    data class Error(val error: String) : GetStoresByCategoryState()
}

sealed class GetStoresByTagState {
    object Idle : GetStoresByTagState()
    object Loading : GetStoresByTagState()
    data class Success(val message: String = "Login Successful") : GetStoresByTagState()
    data class Error(val error: String) : GetStoresByTagState()
}

/*
sealed class GetTrendStoresState {
    object Idle : GetTrendStoresState()
    object Loading : GetTrendStoresState()
    data class Success(val message: String = "Login Successful") : GetTrendStoresState()
    data class Error(val error: String) : GetTrendStoresState()
}
sealed class GetTrendStoresByCategoryState {
    object Idle : GetTrendStoresByCategoryState()
    object Loading : GetTrendStoresByCategoryState()
    data class Success(val message: String = "Login Successful") : GetTrendStoresByCategoryState()
    data class Error(val error: String) : GetTrendStoresByCategoryState()
}
sealed class GetTrendStoresByTagState {
    object Idle : GetTrendStoresByTagState()
    object Loading : GetTrendStoresByTagState()
    data class Success(val message: String = "Login Successful") : GetTrendStoresByTagState()
    data class Error(val error: String) : GetTrendStoresByTagState()
}
*/
sealed class SearchForStoreState {
    object Idle : SearchForStoreState()
    object Loading : SearchForStoreState()
    data class Success(val message: String = "Login Successful") : SearchForStoreState()
    data class Error(val error: String) : SearchForStoreState()
}

sealed class GetWorkShiftsState {
    object Idle : GetWorkShiftsState()
    object Loading : GetWorkShiftsState()
    data class Success(val message: String = "Login Successful") : GetWorkShiftsState()
    data class Error(val error: String) : GetWorkShiftsState()
}

sealed class GetStoreProductsState {
    object Idle : GetStoreProductsState()
    object Loading : GetStoreProductsState()
    data class Success(val message: String = "Login Successful") : GetStoreProductsState()
    data class Error(val error: String) : GetStoreProductsState()
}

sealed class GetCouponDetailsState {
    object Idle : GetCouponDetailsState()
    object Loading : GetCouponDetailsState()
    data class Success(val message: String = "Login Successful") : GetCouponDetailsState()
    data class Error(val error: String) : GetCouponDetailsState()
}

sealed class GetCategoryTagsState {
    object Idle : GetCategoryTagsState()
    object Loading : GetCategoryTagsState()
    data class Success(val message: String = "Login Successful") : GetCategoryTagsState()
    data class Error(val error: String) : GetCategoryTagsState()
}
sealed class SendUserOrderState {
    object Idle : SendUserOrderState()
    object Loading : SendUserOrderState()
    data class Success(val message: String = "Login Successful") : SendUserOrderState()
    data class Error(val error: String) : SendUserOrderState()
}


class HomeScreenViewModel(
    private val visitorRepository: VisitorRepository,
    private val context: Context,
    private var locationCode: String,
    var latitude: Double,
    var longitude: Double
) : ViewModel() {
    private val _language = MutableStateFlow("ar")
    private val language: StateFlow<String> = _language
    val tokenManager = TokenManager(context)

    val token = tokenManager.getToken() ?: ""

    init {
        viewModelScope.launch {
            val lang = LanguagePreferences.get(context).first() ?: "ar"
            _language.value = lang
        }
    }

    private val _getSelectedStore = MutableStateFlow<StoresData?>(null)
    var getSelectedStore: StateFlow<StoresData?> = _getSelectedStore
    fun setSelectedStore(storesData: StoresData) {
        _getSelectedStore.value = storesData
    }
    private val _getCartStore = MutableStateFlow<StoresData?>(null)
    var getCartStore: StateFlow<StoresData?> = _getCartStore
    fun setCartStore(storesData: StoresData) {
        _getCartStore.value = storesData
    }
    private val _getStoreAppliedCoupon = MutableStateFlow<GetCouponDetailsRes?>(null)
    var getStoreAppliedCoupon: StateFlow<GetCouponDetailsRes?> = _getStoreAppliedCoupon
    fun setStoreAppliedCoupon(storesData: StoresData) {
        _getCartStore.value = storesData
    }

    val tagClicked = MutableStateFlow<Boolean>(false)
    fun setTagClicked(value: Boolean) {
        tagClicked.value = value
    }

    val searched = MutableStateFlow<Boolean>(false)
    fun setSearchedClicked(value: Boolean) {
        searched.value = value
    }

    val categoryClicked = MutableStateFlow<Boolean>(false)
    fun setCategoryClicked(value: Boolean) {
        categoryClicked.value = value
    }

    val hideBottomMenu = MutableStateFlow<Boolean>(true)
    fun changeHideBottomMenu() {
        hideBottomMenu.value = !hideBottomMenu.value
    }

    var selectedCategory = MutableStateFlow<CategoryWithTags?>(null)
    fun setSelectedCategory(selectedCategoryParam: CategoryWithTags?) {
        if (selectedCategoryParam != null) {
            selectedCategory.value = selectedCategoryParam
        }
    }

    var selectedProduct = MutableStateFlow<MenuItem?>(null)
    fun setSelectedProduct(selectedProductParam: MenuItem?) {
        if (selectedProductParam != null) {
            selectedProduct.value = selectedProductParam
        }
    }

    private val _getStoreCategoryResponse = MutableStateFlow<List<GetStoreCategoriesRes?>>(listOf())
    var getStoreCategoryResponse = _getStoreCategoryResponse
    private val _getStoreCategoryState =
        MutableStateFlow<GetStoreCategoryState>(GetStoreCategoryState.Idle)
    val getStoreCategoryState: StateFlow<GetStoreCategoryState> = _getStoreCategoryState


    var getNearStoresHasRequested = mutableStateOf(false)

    var getStoreCategoryHasRequested = mutableStateOf(false)
    var getProductRequested = mutableStateOf(false)
    fun getStoresCategories() {
        val params = GetStoreCategoriesParams(language.value)
        viewModelScope.launch {
            _getStoreCategoryState.value = GetStoreCategoryState.Loading

            try {
                val res = visitorRepository.getStoreCategories(params)
                if (res != null) {
                    _getStoreCategoryResponse.value = res
                    _getStoreCategoryState.value = GetStoreCategoryState.Success()

                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getStoreCategoryState.value =
                    GetStoreCategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------
    private val _getNearStoresResponse = MutableStateFlow<GetStoresRes?>(null)
    var getNearStoresResponse = _getNearStoresResponse

    private val _getNearStoresState = MutableStateFlow<GetNearStoresState>(GetNearStoresState.Idle)
    val getNearStoresState: StateFlow<GetNearStoresState> = _getNearStoresState

    fun getNearStores(limit: Int, offset: Int) {
        val params = GetNearStoresParams(
            limit = limit, offset = offset, logitudes = longitude,
            latitudes = latitude, locationCode = locationCode, ln = language.value
        )
        viewModelScope.launch {
            _getNearStoresState.value = GetNearStoresState.Loading

            try {
                var res: GetStoresRes?;
                withContext(Dispatchers.IO) {
                    res = visitorRepository.getNearStores(params)
                }
                if (res != null) {
                    _getNearStoresResponse.value = res
                    _getNearStoresState.value = GetNearStoresState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getNearStoresState.value = GetNearStoresState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------
    private val _getStoresByCategoryResponse = MutableStateFlow<GetStoresRes?>(null)
    var getStoresByCategoryResponse = _getStoresByCategoryResponse

    private val _getStoresByCategoryState =
        MutableStateFlow<GetStoresByCategoryState>(GetStoresByCategoryState.Idle)
    val getStoresByCategoryState: StateFlow<GetStoresByCategoryState> = _getStoresByCategoryState

    fun getNearStoresByCategory(limit: Int, offset: Int, categoryId: String) {
        val params = GetNearStoresByCategoryParams(
            limit = limit,
            offset = offset,
            logitudes = longitude,
            latitudes = latitude,
            locationCode = locationCode,
            categoryId = categoryId,
            ln = language.value
        )
        viewModelScope.launch {
            _getStoresByCategoryState.value = GetStoresByCategoryState.Loading

            try {
                var res: GetStoresRes?
                withContext(Dispatchers.IO) {

                    res = visitorRepository.getStoresByCategory(params)
                }
                if (res != null) {
                    _getStoresByCategoryResponse.value = res

                    _getStoresByCategoryState.value = GetStoresByCategoryState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getStoresByCategoryState.value =
                    GetStoresByCategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }
    //------------------------------------------------------

    private val _getStoresByTagResponse = MutableStateFlow<GetStoresRes?>(null)
    var geStoresByTagResponse = _getStoresByTagResponse

    private val _getStoresByTagState =
        MutableStateFlow<GetStoresByTagState>(GetStoresByTagState.Idle)
    val getStoresByTagState: StateFlow<GetStoresByTagState> = _getStoresByTagState

    fun getNearStoresByTag(limit: Int, offset: Int, tagId: String) {
        val params = GetNearStoresByTagParams(
            limit = limit, offset = offset, logitudes = longitude,
            latitudes = latitude, locationCode = locationCode, tagId = tagId, ln = language.value
        )
        viewModelScope.launch {
            _getStoresByTagState.value = GetStoresByTagState.Loading

            try {
                val res = visitorRepository.getStoresByTag(params)
                if (res != null) {
                    _getStoresByTagResponse.value = res
                    _getStoresByTagState.value = GetStoresByTagState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getStoresByTagState.value = GetStoresByTagState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /*
    //------------------------------------------------------
    private val _getTrendStoresResponse = MutableStateFlow<List<GetStoreCategoriesRes?>>(listOf())
    var getTrendStoresResponse = _getTrendStoresResponse

    private val _getTrendStoresState = MutableStateFlow<GetTrendStoresState>(GetTrendStoresState.Idle)
    val getTrendStoresState: StateFlow<GetTrendStoresState> = _getTrendStoresState

    fun getStoresCategories() {
        val params = GetStoreCategoriesParams(language.value)
        viewModelScope.launch {
            _getTrendStoresState.value = GetTrendStoresState.Loading

            try {
                val res =  visitorRepository.aaa(params)
                if(res != null) {
                    _getTrendStoresResponse.value = res
                    _getTrendStoresState.value = GetTrendStoresState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getTrendStoresState.value = GetTrendStoresState.Error(e.message ?: "Unknown error")
            }
        }
    }
    //------------------------------------------------------
    private val _getTrendStoresByCategoryResponse = MutableStateFlow<List<GetStoreCategoriesRes?>>(listOf())
    var getTrendStoresByCategoryResponse = _getTrendStoresByCategoryResponse

    private val _getTrendStoresByCategoryState = MutableStateFlow<GetTrendStoresByCategoryState>(GetTrendStoresByCategoryState.Idle)
    val getTrendStoresByCategoryState: StateFlow<GetTrendStoresByCategoryState> = _getTrendStoresByCategoryState

    fun getStoresCategories() {
        val params = GetStoreCategoriesParams(language.value)
        viewModelScope.launch {
            _getTrendStoresByCategoryState.value = GetTrendStoresByCategoryState.Loading

            try {
                val res =  visitorRepository.aaa(params)
                if(res != null) {
                    _getTrendStoresByCategoryResponse.value = res
                    _getTrendStoresByCategoryState.value = GetTrendStoresByCategoryState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getTrendStoresByCategoryState.value = GetTrendStoresByCategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }
    //------------------------------------------------------
    private val _getTrendStoresByTagResponse = MutableStateFlow<List<GetStoreCategoriesRes?>>(listOf())
    var getTrendStoresByTagResponse = _getTrendStoresByTagResponse

    private val _getTrendStoresByTagState = MutableStateFlow<GetTrendStoresByTagState>(GetTrendStoresByTagState.Idle)
    val getTrendStoresByTagState: StateFlow<GetTrendStoresByTagState> = _getTrendStoresByTagState

    fun getStoresCategories() {
        val params = GetStoreCategoriesParams(language.value)
        viewModelScope.launch {
            _getTrendStoresByTagState.value = GetTrendStoresByTagState.Loading

            try {
                val res =  visitorRepository.aaa(params)
                if(res != null) {
                    _getTrendStoresByTagResponse.value = res
                    _getTrendStoresByTagState.value = GetTrendStoresByTagState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getTrendStoresByTagState.value = GetTrendStoresByTagState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
     */
    //------------------------------------------------------
    private val _searchForStoreResponse = MutableStateFlow<GetStoresRes?>(null)
    var searchForStoreResponse = _searchForStoreResponse

    private val _searchForStoreState =
        MutableStateFlow<SearchForStoreState>(SearchForStoreState.Idle)
    val searchForStoreState: StateFlow<SearchForStoreState> = _searchForStoreState

    fun searchForStore(limit: Int, offset: Int, storeName: String) {
        val params = SearchForStoresParams(
            limit = limit,
            offset = offset,
            logitudes = longitude,
            latitudes = latitude,
            locationCode = locationCode,
            storeName = storeName,
            ln = language.value
        )
        viewModelScope.launch {
            _searchForStoreState.value = SearchForStoreState.Loading

            try {
                val res = visitorRepository.searchForStore(params)
                _searchForStoreState.value = SearchForStoreState.Success()

                if (res != null) {
                    _searchForStoreResponse.value = res
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _searchForStoreState.value = SearchForStoreState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------
    private val _getWorkShiftsResponse = MutableStateFlow<GetWorkShiftsRes?>(null)
    var getWorkShiftsResponse = _getWorkShiftsResponse

    private val _getWorkShiftsState = MutableStateFlow<GetWorkShiftsState>(GetWorkShiftsState.Idle)
    val getWorkShiftsState: StateFlow<GetWorkShiftsState> = _getWorkShiftsState

    fun getWorkShifts(storeId: String) {
        val params = GetWorkShiftsParams(storeId = storeId)
        viewModelScope.launch {
            _getWorkShiftsState.value = GetWorkShiftsState.Loading

            try {
                var res: GetWorkShiftsRes?
                withContext(Dispatchers.IO) {

                    res = visitorRepository.getWorkShifts(params)
                }
                if (res != null) {
                    _getWorkShiftsResponse.value = res
                    _getWorkShiftsState.value = GetWorkShiftsState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getWorkShiftsState.value = GetWorkShiftsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------
    private val _getStoreProductsResponse = MutableStateFlow<GetStoreProductsRes?>(null)
    var getStoreProductsResponse = _getStoreProductsResponse

    private val _getStoreProductsState =
        MutableStateFlow<GetStoreProductsState>(GetStoreProductsState.Idle)
    val getStoreProductsState: StateFlow<GetStoreProductsState> = _getStoreProductsState

    fun setStoreProductsState(getStoreProductsState: GetStoreProductsState) {
        _getStoreProductsState.value = getStoreProductsState
    }

    private val _getMenuCategoryItems = MutableStateFlow<List<MenuCategory>>(listOf())
    val getMenuCategoryItems: MutableStateFlow<List<MenuCategory>> = _getMenuCategoryItems

    fun getStoreProducts(storeId: String) {
        val params = GetStoreProductsParams(storeId = storeId, ln = language.value)
        viewModelScope.launch {
            _getStoreProductsState.value = GetStoreProductsState.Loading

            try {
                var res: GetStoreProductsRes?
                withContext(Dispatchers.IO) {

                    res = visitorRepository.getStoreProducts(params)
                }
                if (res != null) {
                    _getStoreProductsResponse.value = res
                    _getMenuCategoryItems.value = convertToMenuCategories(res)
                    _getStoreProductsState.value = GetStoreProductsState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getStoreProductsState.value =
                    GetStoreProductsState.Error(e.message ?: "Unknown error")

            }
        }
    }

    //------------------------------------------------------
    private val _getCouponDetailsResponse = MutableStateFlow<GetCouponDetailsRes?>(null)
    var getCouponDetailsResponse = _getCouponDetailsResponse

    private val _getCouponDetailsState =
        MutableStateFlow<GetCouponDetailsState>(GetCouponDetailsState.Idle)
    val getCouponDetailsState: StateFlow<GetCouponDetailsState> = _getCouponDetailsState

    fun getCouponDetails(couponCode: String,storeId:String) {
        val params = GetCouponDetailsParams(couponCode=couponCode,storeId=storeId)
        viewModelScope.launch {
            _getCouponDetailsState.value = GetCouponDetailsState.Loading
            var res: GetCouponDetailsRes? = null
            try {
                withContext(Dispatchers.IO) {

                    res = visitorRepository.getCouponDetails(params)
                }
                if (res != null) {
                    _getCouponDetailsResponse.value = res
                    _getCouponDetailsState.value = GetCouponDetailsState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getCouponDetailsState.value =
                    GetCouponDetailsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------
    private val _getCategoryTagsResponse = MutableStateFlow<List<GetCategoryTagsRes?>>(listOf())
    var getCategoryTagsResponse = _getCategoryTagsResponse

    private val _getCategoryTagsState =
        MutableStateFlow<GetCategoryTagsState>(GetCategoryTagsState.Idle)
    val getCategoryTagsState: StateFlow<GetCategoryTagsState> = _getCategoryTagsState

    fun getCategoryTags(categoryId: String) {
        val params = GetCategoryTagsParams(categoryId = categoryId, ln = language.value)
        viewModelScope.launch {
            _getCategoryTagsState.value = GetCategoryTagsState.Loading
            var res: List<GetCategoryTagsRes>?
            try {
                withContext(Dispatchers.IO) {

                    res = visitorRepository.getCategoryTags(params)
                }
                if (res != null) {
                    _getCategoryTagsResponse.value = res as List<GetCategoryTagsRes>
                    _getCategoryTagsState.value = GetCategoryTagsState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getCategoryTagsState.value =
                    GetCategoryTagsState.Error(e.message ?: "Unknown error")
            }
        }
    }
//-----------------------------------------------------------------------------
private val _sendUserOrderResponse = MutableStateFlow<PostVirtualRes?>(null)
    var sendUserOrderResponse = _sendUserOrderResponse

    private val _sendUserOrderState =
        MutableStateFlow<SendUserOrderState>(SendUserOrderState.Idle)
    val sendUserOrderState: StateFlow<SendUserOrderState> = _sendUserOrderState

    fun sendUserOrder() {
        val params =
            SendUserOrderParams(
                longitudes = longitude,
                latitudes =latitude,
                couponCode =storeOrder.coupon,
                store_id = storeOrder.store_id,
                orders_type =storeOrder.order_type, delivery_note =storeOrder.delivery_note,
                totalPrice =storeOrder.totalPrice,
                items =storeOrder.orderItems )

        viewModelScope.launch {
            _sendUserOrderState.value = SendUserOrderState.Loading
            var res: PostVirtualRes?
            try {
                withContext(Dispatchers.IO) {

                    res = params?.let { visitorRepository.sendUserOrder(it,token) }
                }
                if (res != null) {
                    _sendUserOrderResponse.value = res as PostVirtualRes
                    _sendUserOrderState.value = SendUserOrderState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _getCategoryTagsState.value =
                    GetCategoryTagsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //-----------------------------------------------------------------------------
    private val _categoriesWithTags = MutableStateFlow<CategoriesWithTags?>(null)
    val categoriesWithTags: StateFlow<CategoriesWithTags?> = _categoriesWithTags

    fun loadCategoriesWithTags() {
        val params = GetStoreCategoriesParams(language.value)

        viewModelScope.launch {
            _getStoreCategoryState.value = GetStoreCategoryState.Loading

            try {
                val categoriesRes = visitorRepository.getStoreCategories(params)

                if (!categoriesRes.isNullOrEmpty()) {
                    val filteredCategories = categoriesRes.filterNotNull()
                    val categoryWithTagsList = mutableListOf<CategoryWithTags>()


                    for (category in filteredCategories) {

                        val tagsRes = visitorRepository.getCategoryTags(
                            GetCategoryTagsParams(
                                categoryId = category.category_id,
                                ln = language.value
                            )
                        )

                        categoryWithTagsList.add(
                            CategoryWithTags(
                                categories = category,
                                tags = tagsRes ?: emptyList()
                            )
                        )
                    }

                    _categoriesWithTags.value =
                        CategoriesWithTags(categories = categoryWithTagsList)
                    _getStoreCategoryState.value = GetStoreCategoryState.Success()
                } else {
                    _getStoreCategoryState.value = GetStoreCategoryState.Error("قائمة الفئات فارغة")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "خطأ أثناء تحميل الفئات أو التاجات: $e")
                _getStoreCategoryState.value =
                    GetStoreCategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setLocation(entry: LocationEntry) {
        latitude = entry.latitude
        longitude = entry.longitude
        locationCode = encodeToQuadrants(latitude = entry.latitude, longitude = entry.longitude)
    }

    //--------------------------order and card-----------------------------------
    var lastId = -1;
    fun getNewId(): Int {
        lastId++
        return lastId;
    }

    val orderItems=   mutableStateListOf<OrderItem>()


    var storeOrder: OrderData = OrderData(store_id = getCartStore.value?.store_id ?: "-1",delivery_note ="",
        order_type = OrdersType.DELIVERY,coupon="",totalPrice=0.0, orderItems = orderItems)
    fun addToCart(orderItem: OrderItem) {
        orderItems.add(orderItem)
    }


    var firstSizeBuild = mutableStateOf(true)

    var selectedModifiers = MutableStateFlow(mutableListOf<ModifierItemOrderProduct>())


    val totalPrice = MutableStateFlow<Double>(0.0)
    fun setTotalPrice(total:Double){
        totalPrice.value=total
    }


    //    fun increaseTotalPrice (amount :Double){
//        totalPrice.value =amount
//    }
//

    var selectedItem = MutableStateFlow<OrderItem>(
        OrderItem(
            internalId = -1,
            item_name = "name",
            item_id = "-1",
            size_name = "size",
            size_Price = 0.0,
            size_id = "-1",
            modifiers = listOf(),
            count = 1,
            note = ""
        )
    )
//
//    fun setSelectedOrderItem(OrderItemParam: OrderItem?) {
//        if (OrderItemParam != null) {
//            selectedItem.value = OrderItemParam
//        }
//    }
//
//

//

//

//
//    fun removeFromCart(itemIndex: Int) {
//        orderItems.forEachIndexed { index, orderItem ->
//            if (orderItem.internalId == itemIndex) {
//                orderItems.removeAt(index)
//                return
//            }
//        }
//    }
//
//    fun decreaseItemCount(itemIndex: Int) {
//        orderItems.forEachIndexed { index, orderItem ->
//            if (orderItem.internalId == itemIndex) {
//                orderItem.count--
//                return
//            }
//        }
//    }
//
//    fun increaseItemCount(itemIndex: Int) {
//        orderItems.forEachIndexed { index, orderItem ->
//            if (orderItem.internalId == itemIndex) {
//                orderItem.count++
//                return
//            }
//        }
//    }
//
//    fun isCartEmpty(): Boolean {
//        return orderItems.isEmpty()
//    }
}
