package com.lightning.androidfrontend.utils

import com.lightning.androidfrontend.data.model.GetStoreProductsRes
import com.lightning.androidfrontend.data.model.MenuCategory
import com.lightning.androidfrontend.data.model.MenuItem
import com.lightning.androidfrontend.data.model.MenuItemSize
import com.lightning.androidfrontend.data.model.MenuModifier
import com.lightning.androidfrontend.data.model.MenuModifierItem
import com.lightning.androidfrontend.data.model.ModifierItemOrderProduct
import com.lightning.androidfrontend.data.model.OrderItem
import com.lightning.androidfrontend.data.model.OrderModifier
import com.lightning.androidfrontend.data.model.OrderModifierItem

fun convertToMenuCategories(
    Menu: GetStoreProductsRes?,

    ): List<MenuCategory> {
    if (Menu != null) {
        val menu = Menu.products.category.map { myCategory ->
            val categoryItems = Menu.products.items.filter { it.category_id == myCategory.category_id }.map { item ->
                val menuSizes = item.sizes.map { size ->
                    val sizeModifiers = size.modifiers_id.mapNotNull { modifierId ->
                        Menu.products.modifiers.find { it.modifiers_id == modifierId }?.let { modifier ->
                            MenuModifier(
                                modifiers_id = modifier.modifiers_id,
                                title = modifier.title,
                                label = modifier.label,
                                type = modifier.type,
                                min = modifier.min,
                                max = modifier.max,
                                items = modifier.items.map { modifierItem ->
                                    MenuModifierItem(
                                        name = modifierItem.name,
                                        order = modifierItem.order,
                                        price = modifierItem.price,
                                        is_enable = modifierItem.is_enable,
                                        is_default = modifierItem.is_default,
                                        modifiers_item_id = modifierItem.modifiers_item_id
                                    )
                                }
                            )
                        }
                    }

                    MenuItemSize(
                        name = size.name,
                        order = size.order,
                        price = size.price,
                        size_id = size.size_id,
                        calories = size.calories,
                        modifiers = sizeModifiers
                    )
                }

                MenuItem(
                    item_id = item.item_id,
                    name = item.name,
                    description = item.description,
                    image_url = item.image_url,
                    allergens = item.allergens,
                    category_id = item.category_id,
                    order = item.order,
                    is_activated = item.is_activated,
                    is_best_seller = item.is_best_seller,
                    external_price = item.external_price,
                    internal_item_id = item.internal_item_id,
                    sizes = menuSizes
                )
            }

            MenuCategory(
                name = myCategory.name,
                order = myCategory.order,
                category_id = myCategory.category_id,
                items = categoryItems
            )
        }
        return  sortMenuCategories(menu)
    }
    return listOf()
}
fun sortMenuCategories(categories: List<MenuCategory>): List<MenuCategory> {
    return categories
        .sortedBy { it.order } // 1. ترتيب الكاتيجوري
        .map { category ->
            category.copy(
                items = category.items
                    .sortedBy { it.order } // 2. ترتيب العناصر
                    .map { item ->
                        item.copy(
                            sizes = item.sizes
                                .sortedBy { it.order } // 3. ترتيب الأحجام
                                .map { size ->
                                    size.copy(
                                        modifiers = size.modifiers.map { modifier ->
                                            modifier.copy(
                                                items = modifier.items.sortedBy { it.order } // 4. ترتيب موديفاير آيتيمز
                                            )
                                        }
                                    )
                                }
                        )
                    }
            )
        }
}
//---------------------------------------------------
fun groupToOrderModifiers(input: List<ModifierItemOrderProduct>): List<OrderModifier> {
    return input
        .groupBy { it.parentId }
        .map { (parentId, items) ->
            OrderModifier(
                modifiers_id = parentId,
                title = items.first().NameParent,
                items = items.map {
                    OrderModifierItem(
                        name = it.modifier_item_name,
                        price = it.modifier_item_Price,
                        modifiers_item_id = it.modifier_item_id,
                        number = it.number
                    )
                }
            )
        }
}
