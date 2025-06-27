package com.lightning.androidfrontend.data.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GetStoreCategoriesParams(
    val ln: String,
)
data class CategoriesWithTags(
    val categories:List<CategoryWithTags>,


)
data class CategoryWithTags(
    val categories:GetStoreCategoriesRes,
    val tags:List<GetCategoryTagsRes>

)
data class GetStoreCategoriesRes(
    val category_id: String,
    val category_name: String,
    val category_image: String,

)
//------------------------------------------------------------
data class GetCategoryTagsParams(
    val ln: String,
    val categoryId :String
)
data class GetCategoryTagsRes(
    val tag_id: String,
    val tag_name: String,

    )
//------------------------------------------------------------

data class GetNearStoresParams(
    val limit: Int,
    val offset :Int,
    val logitudes: Double,
    val latitudes :Double,
    val locationCode: String,
    val ln :String
)
data class GetStoresRes(
    val hasNext: Boolean,
    val trendStores: List<StoresData>,
    val stores: List<StoresData>,
    )
data class StoresData(
    val store_id: String,
    val title: String,
    val tags: List<String>,
    val status: String,
    val isOpen :Boolean,
    val delivery_price: Double,
    val min_order_price: Double,
    val distance_km: Double,
    val preparation_time: Int,
    val rating_previous_day: Double,
    val number_of_raters: Int,
    val logo_image_url: String,
    val cover_image_url: String,
    val orders_type: OrdersType,
    val couponCode: String?,
    val delivery_discount_percentage: Double?,
    val discount_value_percentage: Double?,
    val coupon_min_order_value: Double?,
    )
enum class OrdersType(val label: String) {
    TAKE_AWAY("take_away"),
    DELIVERY("delivery"),
    TAKE_AWAY_AND_DELIVERY("take_away_and_delivery");
    override fun toString(): String {
        return label
    }
    //val type = OrdersType.CURRENT
}
//------------------------------------------------------------

data class GetNearStoresByCategoryParams(
    val limit: Int,
    val offset :Int,
    val logitudes: Double,
    val latitudes :Double,
    val locationCode: String,
    val ln :String,
    val categoryId :String

)
//------------------------------------------------------------

data class GetNearStoresByTagParams(
    val limit: Int,
    val offset :Int,
    val logitudes: Double,
    val latitudes :Double,
    val locationCode: String,
    val ln :String,
    val tagId :String

)
//------------------------------------------------------------

enum class DayOfWeek(val label: String) {
    SUN("Sun"),
    MON("Mon"),
    TUE("Tue"),
    WED("Wed"),
    THU("The"),
    FRI("Fri"),
    SAT("Sat");
    override fun toString(): String {
        return label
    }
}

data class GetWorkShiftsParams(
    val storeId :String

)
data class GetWorkShiftsRes(
    @SerializedName("working_time")
    val workingTime: Map<DayOfWeek, List<WorkingPeriod>>
)

data class WorkingPeriod(
    @SerializedName("opening_time")
    val openingTime: String,

    @SerializedName("closing_time")
    val closingTime: String
)

//------------------------------------------------------------
data class GetStoreProductsRes(
    val products: MenuData

)
data class GetStoreProductsParams(
    val storeId :String,
    val ln :String

)
data class MenuData(
    val items: List<Item>,
    val category: List<Category>,
    val modifiers: List<Modifier>
)

data class Category(
    val name: String,
    val order: Int,
    val category_id: String
)

data class Item(
    val item_id: String,
    val name: String,
    val description: String,
    val image_url: String,
    val allergens: List<String>,
    val category_id: String,
    val order: Int,
    val is_activated: Boolean,
    val is_best_seller: Boolean,
    val external_price: String,
    val internal_item_id: Int,
    val sizes: List<ItemSize>
)

data class ItemSize(
    val name: String,
    val order: Int,
    val price: Double,
    val size_id: String,
    val calories: Int,
    val modifiers_id: List<String>
)

data class Modifier(
    val modifiers_id: String,
    val title: String,
    val label: String,
    val type: ModifierType,
    val min: Int,
    val max: Int,
    val items: List<ModifierItem>
)

data class ModifierItem(
    val name: String,
    val order: Int,
    val price: Double,
    val is_enable: Boolean,
    val is_default: Boolean,
    val modifiers_item_id: String
)
enum class ModifierType(val label: String) {
    Optional("Optional"),
    Multiple("Multiple");

    override fun toString(): String {
        return label
    }
}
//-----------------------------------------------------------
data class MenuCategory(
    val name: String,
    val order: Int,
    val category_id: String,
    val items: List<MenuItem>

)

data class MenuItem(
    var item_id: String,
    val name: String,
    val description: String,
    val image_url: String,
    val allergens: List<String>,
    val category_id: String,
    val order: Int,
    val is_activated: Boolean,
    val is_best_seller: Boolean,
    val external_price: String,
    val internal_item_id: Int,
    val sizes: List<MenuItemSize>

)

data class MenuItemSize(
    val name: String,
    val order: Int,
    val price: Double,
    val size_id: String,
    val calories: Int,
    val modifiers: List<MenuModifier>
)

data class MenuModifier(
    val modifiers_id: String,
    val title: String,
    val label: String,
    val type: ModifierType,
    val min: Int,
    val max: Int,
    val items: List<MenuModifierItem>
)

data class MenuModifierItem(
    val name: String,
    val order: Int,
    val price: Double,
    val is_enable: Boolean,
    val is_default: Boolean,
    val modifiers_item_id: String
)



//------------------------------------------------------------

data class OrderData(
    val store_id:String,
    val orderItems:  List<OrderItem>,
    val coupon :String,
    var totalPrice :Double,
    var delivery_note : String,
    var order_type : OrdersType

)
data class OrderItem(
    var internalId :Int,
    var item_name :String,
    var item_id: String,
    var size_name :String,
    var size_Price :Double,

    var size_id: String,
    var modifiers: List<OrderModifier>,
    var count :Int,
    val note: String,
)

data class OrderModifier(
    val modifiers_id: String,
    val title: String,
    val items: List<OrderModifierItem>

)

data class OrderModifierItem(
    val name: String,
    val price: Double,
    val modifiers_item_id: String,
    val number: Int

)


data class ModifierItemOrderProduct(
    val parentId: String,
    val NameParent: String,

    val internalId: Int,
    val modifier_item_name: String,

    val modifier_item_Price: Double,

    val modifier_item_id: String,
    val number: Int
)
data class OrderInput(
    val items :List<OrderItem>,

)
//--------------------------------------------------------
data class TotalResolved(
    @SerializedName("orderAR") val orderAR: List<ResolvedOrderItem>,
    @SerializedName("orderEn") val orderEn: List<ResolvedOrderItem>,
    @SerializedName("delivery_note") val deliveryNote: String,
    @SerializedName("total_price") val totalPrice: Double
)

data class ResolvedOrderItem(
    @SerializedName("item_name") val itemName: String,
    @SerializedName("size_name") val sizeName: String,
    @SerializedName("size_price") val sizePrice: Double,
    @SerializedName("modifiers") val modifiers: List<ResolvedModifier>,
    @SerializedName("count") val count: Int,
    @SerializedName("note") val note: String
)

data class ResolvedModifier(
    @SerializedName("title") val title: String,
    @SerializedName("items") val items: List<ResolvedModifierItem>
)

data class ResolvedModifierItem(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("number") val number: Int
)


//--------------------------------------------------------

data class UserOrder(
    val order_id : String,
    val status : OrderStatus,
    val order_details_text :String,
    val store_name_ar : String,
    val store_name_en : String,
    val location_latitude : String,
    val location_longitude : String,
    val orders_type : OrdersType,
    val payment_method : String,
    val amount : Double,
    val delivery_fee : Double,
    val coupon_code : String,

    val created_at : String,
    )

data class UserOrdersRes(
    val hasNext: Boolean,
    val order :List<UserOrder>,

    )

enum class OrderStatus(val label: String) {
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    WITH_DRIVER("with_driver"),
    DELEVERED("delivered"),
    CUSTOMER_NOT_RECIVED("customer_not_Received"),
    DRIVER_NOT_RECIVED("driver_not_Received");
    override fun toString(): String {
        return label
    }
    //val type = OrdersType.CURRENT
}

//--------------------------------------------------------
data class LimitDateOffset(
    val limit: Int,
    val offset:String,
)

//--------------------------------------------------------

data class SendUserOrderParams(
    val longitudes: Double,
    val latitudes:Double,
    val couponCode: String?,
    val store_id: String,
    val orders_type: OrdersType,
    val delivery_note:String,
    val totalPrice:Double,
    val items: List<OrderItem>
)
//------------------------------------------------------------



data class GetCouponDetailsRes(
    val delivery_discount_percentage : Double,
    val discount_percentage: Double,
    var min_order_value: Double

)

data class GetCouponDetailsParams(

    val couponCode: String,
    val storeId :String
)
//------------------------------------------------------------
data class SearchForStoresParams(
    val storeName :String,
    val limit: Int,
    val offset :Int,
    val logitudes: Double,
    val latitudes :Double,
    val locationCode: String,
    val ln :String

)
//-----------------------------------------------
data class LocationEntry(
    val name: String,
    var selected :Boolean,
    val latitude: Double,
    val longitude: Double
)

//-----------------------------------------------
