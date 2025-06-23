package com.lightning.androidfrontend.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import coil.compose.AsyncImage

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.data.model.MenuItemSize
import com.lightning.androidfrontend.data.model.MenuModifier
import com.lightning.androidfrontend.data.model.MenuModifierItem
import com.lightning.androidfrontend.data.model.ModifierItemOrderProduct
import com.lightning.androidfrontend.data.model.OrderItem
import com.lightning.androidfrontend.theme.LocalAppColors
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.theme.black
import com.lightning.androidfrontend.theme.cabinFontFamily
import com.lightning.androidfrontend.theme.metropolisFontFamily
import com.lightning.androidfrontend.theme.orange
import com.lightning.androidfrontend.theme.primaryFontColor
import com.lightning.androidfrontend.theme.secondaryFontColor
import com.lightning.androidfrontend.theme.white
import com.lightning.androidfrontend.ui.screens.ButtonWithImage
import com.lightning.androidfrontend.ui.screens.FilledButton
import com.lightning.androidfrontend.ui.screens.FilledIconButton
import com.lightning.androidfrontend.ui.screens.IconButton
import com.lightning.androidfrontend.utils.groupToOrderModifiers
import com.lightning.androidfrontend.utils.priceText
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ProductDetailsScreen(
    homeScreenViewModel: HomeScreenViewModel,
    navController: NavController

) {

    val selectedItem by homeScreenViewModel.selectedItem.collectAsState()

    val selectedModifiers by homeScreenViewModel.selectedModifiers.collectAsState()
    val colors = LocalAppColors.current

    val scrollState = rememberScrollState()


    val selectedProduct by homeScreenViewModel.selectedProduct.collectAsState()

    val stateChanged = remember { mutableStateOf(0) }
    var externalPrice: Double = 99999999.0
    var showHint by remember { mutableStateOf(false) }
    selectedProduct?.sizes?.forEach { size ->
        if (externalPrice > size.price)
            externalPrice = size.price
    }
        selectedItem.item_id = selectedProduct?.item_id!!
        selectedItem.item_name = selectedProduct?.name!!
        selectedItem.internalId = homeScreenViewModel.getNewId()
    
//    var modifiersPrice = 0.0;
//    selectedItem.modifiers.forEach { modifier ->
//        modifiersPrice += modifier.modifier_item_Price * modifier.number
//    }
//
//    totalPrice = (selectedItem.size_Price + modifiersPrice) * selectedItem.count

    MyAppTheme() {
        Box() {

            AsyncImage(
                model = selectedProduct?.image_url,
                contentDescription = "image_url",

                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                contentScale = ContentScale.Crop
            )
            BlackShadow2()
//            AppTopBar(title = "")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .background(white, shape = RoundedCornerShape(topStart = 42.dp, topEnd = 42.dp))
                    .align(Alignment.BottomCenter)
            ) {
                Column(modifier = Modifier.verticalScroll(state = scrollState)) {
                    selectedProduct?.let {
                        Text(
                            it.name,
                            modifier = Modifier.padding(top = 29.dp, start = 20.dp),
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontFamily = metropolisFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = primaryFontColor
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.55f))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = priceText(externalPrice),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = metropolisFontFamily,
                                    color = primaryFontColor,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    /*   Text(
                          modifier = Modifier.padding(horizontal = 20.dp),
                          text = "الوصف",
                          style = TextStyle(
                              fontSize = 14.sp,
                              fontFamily = metropolisFontFamily,
                              color = primaryFontColor,
                              fontWeight = FontWeight.Bold
                          )
                      )*/
                    Spacer(modifier = Modifier.height(10.dp))
                    selectedProduct?.let {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = it.description,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = metropolisFontFamily,
                                color = secondaryFontColor
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(17.dp))
                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = colors.productDetailsColor
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = colors.productDetailsColorLighter, // اللون البرتقالي
                                shape = RoundedCornerShape(12.dp) // الزوايا المنحنية
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp) // padding داخل المربع
                    ) {
                        Text(
                            modifier = Modifier
                                .clickable { showHint = !showHint }
                                .padding(horizontal = 20.dp),
                            text = "مسببات الحساسية ",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = metropolisFontFamily,
                                color = primaryFontColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    if (showHint) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = colors.productDetailsColorLighter,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 24.dp, vertical = 16.dp)
                            ) {
                                selectedProduct?.allergens?.forEach { allergen ->
                                    Text(
                                        text = allergen,
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }


                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "اختر الحجم",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = metropolisFontFamily,
                            color = primaryFontColor,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    selectedProduct?.let {
                        MenuListWithSingleSelection(
                            sizes = it.sizes,
                            homeScreenViewModel = homeScreenViewModel,
                            state = stateChanged.value,
                            onSelectedItemChange ={},// { selectedItem = it },
                            selectedItem=selectedItem,selectedModifiers=selectedModifiers
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))


                    Spacer(modifier = Modifier.height(24.dp))
                    NumberOfItem(homeScreenViewModel = homeScreenViewModel,
                        state = stateChanged.value,
                        onSelectedItemChange ={},// { selectedItem = it }

                        selectedItem = selectedItem)



                    Spacer(modifier = Modifier.height(27.dp))

                    TotalPrice(
                        homeScreenViewModel = homeScreenViewModel,
                        totalPrice = 0.0,
                        selectedItem = selectedItem,
                        state = stateChanged.value,
                        onSelectedItemChange ={},// { selectedItem = it },
                        navController=navController
                    )
                }


            }
//            Box(
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(end = 30.dp)
//            ) {
//                Column() {
//                    Spacer(modifier = Modifier.fillMaxHeight(0.315f))
//                    IconButton(image = R.drawable.ic_favorite) {}
//                }
//            }
        }
    }
}

@Composable
fun BlackShadow2() {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        black.copy(alpha = 0.3f),
                        black.copy(alpha = 0f),
                    )
                )
            )
    )
}

@Composable
fun TotalPrice(
    homeScreenViewModel: HomeScreenViewModel,
    totalPrice: Double = 1.0,
    selectedItem: OrderItem,
    state: Int,
    onSelectedItemChange: (OrderItem) -> Unit,
    navController: NavController

) {
    val selectedItem by homeScreenViewModel.selectedItem.collectAsState()

    val totalPrice by homeScreenViewModel.totalPrice.collectAsState()

    val selectedModifiers by homeScreenViewModel.selectedModifiers.collectAsState()



    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(180.dp)
                .width(95.dp)
                .background(
                    color = orange,
                    shape = RoundedCornerShape(topEnd = 38.dp, bottomEnd = 38.dp)
                )
        ) {}

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        bottomStart = 40.dp,
                        topEnd = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .background(color = white)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 55.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "السعرالاجمالي للعنصر", style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = metropolisFontFamily,
                        color = primaryFontColor,
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = priceText(totalPrice),
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontFamily = metropolisFontFamily,
                        color = primaryFontColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(text = state.toString())
                Spacer(modifier = Modifier.height(10.dp))
                ButtonWithImage(
                    text = "اضف الى السلة",
                    image = R.drawable.ic_buy,
                    color = orange,
                    modifier = Modifier
                        .height(40.dp)
                        .width(200.dp)
                ) {

                    selectedItem.let {
                        onSelectedItemChange(
                            selectedItem.copy( )
                        )
                        
                        homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
                        homeScreenViewModel.addToCart(selectedItem)
                        Log.d("debugTag", "totalPrice : $totalPrice")

                        Log.d("debugTag", "selectedItem : $selectedItem")
                        selectedItem.modifiers= groupToOrderModifiers(selectedModifiers.toList())
                    }

                    //navController.popBackStack()

                }
                Spacer(modifier = Modifier.height( 24.dp))

            }

            Icon(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = "سلة التسوق",
                tint = Color.White
            )
        }
    }
}
//TOdO make all change on SelectedItem by onSelectedItemChange
@Composable
fun NumberOfItem(
    homeScreenViewModel: HomeScreenViewModel,
    state: Int,
    onSelectedItemChange: (OrderItem) -> Unit,
    selectedItem: OrderItem

) {


    var count by remember { mutableStateOf(selectedItem.count) }
    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Number of Portions",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = metropolisFontFamily,
                color = primaryFontColor,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(50.dp))
        Spacer(modifier = Modifier.width(7.dp))
        FilledIconButton(icon = Icons.Filled.RemoveCircle) {
            if (count > 1) {
                count--
                selectedItem.count -= 1

                onSelectedItemChange(
                    selectedItem.copy( )
                )
                homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
            }
        }
        Spacer(modifier = Modifier.width(7.dp))
        Box(
            modifier = Modifier
                .width(47.dp)
                .height(30.dp)
                .border(
                    width = 1.dp,
                    color = orange,
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = cabinFontFamily,
                    color = orange,
                )
            )
        }
        Spacer(modifier = Modifier.width(7.dp))
        FilledIconButton(icon = Icons.Filled.AddCircle) {
            selectedItem.count += 1
            count++
            onSelectedItemChange(
                selectedItem.copy( )
            )
            homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))

        }
    }
}

@Composable
fun MenuListWithSingleSelection(
    sizes: List<MenuItemSize>,
    homeScreenViewModel: HomeScreenViewModel,
    state: Int,
    onSelectedItemChange: (OrderItem) -> Unit,
    selectedItem: OrderItem,
    selectedModifiers:MutableList<ModifierItemOrderProduct>

) {

    // حالة العنصر المحدد
    var selectedItemId by remember { mutableStateOf<String?>(if (sizes.isNotEmpty()) sizes[0].size_id else null) }
    var selectedSize by remember { mutableStateOf<MenuItemSize?>(if (sizes.isNotEmpty()) sizes[0] else null) }
    if(homeScreenViewModel.firstSizeBuild.value){
    selectedItem.size_id = selectedSize?.size_id ?: ""
    selectedItem.size_Price = selectedSize?.price?:0.0
    selectedItem.size_name = selectedSize?.name ?: ""
        homeScreenViewModel.firstSizeBuild.value=false
        homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
    }
    Column(modifier = Modifier.padding(16.dp)) {
        sizes.forEachIndexed { index, size ->

            MenuItemCardSelectable(
                size = size,
                isSelected = size.size_id == selectedItemId.orEmpty(),
                onSelect = {
                    selectedModifiers.clear()
                    selectedItemId = size.size_id
                    selectedItem.size_id = size.size_id
                    selectedItem.size_Price = size.price
                    selectedItem.size_name = size.name
                    onSelectedItemChange(
                        selectedItem.copy( )
                    )
                    selectedSize =size
                    homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
                    Log.d("debugTag", "selectedItem : ${selectedItem.size_name}")



                },
                state = state,
                onSelectedItemChange = onSelectedItemChange,
                homeScreenViewModel = homeScreenViewModel, selectedItem = selectedItem
            )

        }
        selectedSize?.modifiers?.forEach { modifier ->
            {}
            if (modifier.type.label == "Multiple")
                MultipleSelectableItemCard(
                    modifier = modifier,
                    homeScreenViewModel = homeScreenViewModel,
                    selectedItem=selectedItem,
                    selectedModifiers=selectedModifiers,
                    state = state,
                    onSelectedItemChange = onSelectedItemChange
                )
            else {
                var selectedItems = mutableListOf<MenuModifierItem>()
                modifier.items.forEach { item ->
                    if (item.is_default)
                        selectedItems.add(item)
                }

                OptionalSelectableItemCard(
                    modifier = modifier,
                    defaultSelectedItems = selectedItems,
                    homeScreenViewModel = homeScreenViewModel,
                    selectedItem=selectedItem,
                    selectedModifiers=selectedModifiers,
                    state = state,
                    onSelectedItemChange = onSelectedItemChange
                )
            }
        }
    }
}

@Composable
fun MenuItemCardSelectable(
    size: MenuItemSize,
    isSelected: Boolean,
    onSelect: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel,
    selectedItem: OrderItem,
    state: Int,
    onSelectedItemChange: (OrderItem) -> Unit,
) {
    val colors = LocalAppColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onSelect() }
            .border(
                width = 2.dp,
                color = colors.productDetailsColor,
                shape = RoundedCornerShape(8.dp)

            ),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = size.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "السعر: ${priceText(size.price)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "السعرات الحرارية: ${size.calories}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Checkbox(
                colors = CheckboxDefaults.colors(checkedColor = colors.productDetailsColorLighter),
                checked = isSelected,
                onCheckedChange = {
                    if (isSelected) {
                        selectedItem.size_id ?: size.size_id
                        selectedItem.size_Price ?: size.price
                        selectedItem.size_name ?: size.price
                            onSelectedItemChange(
                                selectedItem.copy( )
                            )
                        homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
                    }
                }
            )
        }
    }
}

@Composable
fun MultipleSelectableItemCard(
    modifier: MenuModifier,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onCheckChange: (Boolean) -> Unit = {},
    homeScreenViewModel: HomeScreenViewModel,
    selectedItem:OrderItem,
    selectedModifiers:MutableList<ModifierItemOrderProduct>,
    state: Int,
    onSelectedItemChange: (OrderItem) -> Unit,
) {



//    val itemCounters = remember {
//
//        homeScreenViewModel.itemCounters
//    }
//
//    itemCounters.apply {
//        modifier.items.forEach {
//            put(it, mutableStateOf(0))
//        }
//    }
    val itemCounters = remember(modifier) {
        mutableStateMapOf<MenuModifierItem, MutableState<Int>>().apply {
            modifier.items.forEach {
                put(it, mutableStateOf(0))
            }
        }
    }

    val totalCount = remember {
        derivedStateOf {
            itemCounters.values.sumOf { it.value }
        }
    }
    val colors = LocalAppColors.current
    val min = modifier.min
    val max = modifier.max
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, color = colors.productDetailsColor, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
    ) {
        Column {
            Row(modifier = Modifier.padding(3.dp)) {
                Text(
                    text = modifier.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.productDetailsColor
                )
                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(1.dp))

                val annotatedType = buildAnnotatedString {
                    append("النوع: \n")
                    withStyle(
                        style = SpanStyle(
                            background = colors.productDetailsColorLighter,
                            color = white
                        )
                    ) {
                        append("متعدد ")


                    }
                }
                Spacer(modifier = Modifier.width(1.dp))

                val annotatedMin = buildAnnotatedString {
                    append("الحد الادنى: \n")
                    withStyle(
                        style = SpanStyle(
                            background = colors.productDetailsColorLighter,
                            color = white
                        )
                    ) {
                        append(" ${modifier.min} ")
                    }
                }
                Spacer(modifier = Modifier.width(1.dp))

                val annotatedMax = buildAnnotatedString {
                    append("الحد الأقصى: \n")
                    withStyle(
                        style = SpanStyle(
                            background = colors.productDetailsColorLighter,
                            color = white
                        )
                    ) {
                        append(" ${modifier.max} ")
                    }
                }
                Text(
                    text = annotatedMin,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,

                    )

                Text(
                    text = annotatedMax,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,

                    )
                Text(
                    text = annotatedType,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,

                    )


            }
            Spacer(modifier = Modifier.height(8.dp))

            modifier.items.forEach { modifierItem ->
                val countState = itemCounters[modifierItem] ?: mutableStateOf(0)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {

                        Text(
                            text = modifierItem.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = priceText(modifierItem.price),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (countState.value > 0 &&
                                    totalCount.value > modifier.min
                                ) {
                                    countState.value--
                                    onDecrement()

                                    onSelectedItemChange(
                                        selectedItem.copy( )
                                    )
                                    selectedModifiers.clear()
                                    itemCounters.forEach { (modifierItem, counterState) ->
                                        selectedModifiers.add(
                                            ModifierItemOrderProduct(
                                                internalId = homeScreenViewModel.getNewId(),
                                                parentId = modifier.modifiers_id,
                                                NameParent = modifier.title,
                                                modifier_item_name = modifierItem.name,
                                                modifier_item_Price = modifierItem.price,
                                                modifier_item_id = modifierItem.modifiers_item_id,
                                                number = counterState.value
                                            )
                                        )
                                    }
                                    homeScreenViewModel.setTotalPrice(sumTotalItemPrice(selectedItem))
                                    //     selectedModifiers.set(modifierItem, countState.value)
                                }
                            },
                            enabled = countState.value > 0 && totalCount.value > modifier.min
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveCircle,
                                contentDescription = "إنقاص"
                            )
                        }

                        Text(
                            text = countState.value.toString(),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = {
                                if (totalCount.value < modifier.max) {
                                    countState.value++
                                    onIncrement()
                                    onSelectedItemChange(
                                        selectedItem.copy( )
                                    )
                                    selectedModifiers.clear()
                                    itemCounters.forEach { (modifierItem, counterState) ->
                                        selectedModifiers.add(
                                            ModifierItemOrderProduct(
                                                internalId = homeScreenViewModel.getNewId(),
                                                parentId = modifier.modifiers_id,
                                                NameParent = modifier.title,
                                                modifier_item_name = modifierItem.name,
                                                modifier_item_Price = modifierItem.price,
                                                modifier_item_id = modifierItem.modifiers_item_id,
                                                number = counterState.value
                                            )
                                        )
                                    }
                                    homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
                                    //     selectedModifiers.set(modifierItem, countState.value)
                                }
                            },
                            enabled = totalCount.value < modifier.max
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "زيادة"
                            )
                        }
                    }
                }
            }

        }
    }

}


@Composable
fun OptionalSelectableItemCard(
    modifier: MenuModifier,
    defaultSelectedItems: List<MenuModifierItem>,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onCheckChange: (Boolean) -> Unit = {},
    homeScreenViewModel: HomeScreenViewModel,
    selectedItem:OrderItem,
    selectedModifiers:MutableList<ModifierItemOrderProduct>,
    state: Int,
    onSelectedItemChange: (OrderItem) -> Unit,
) {

    val colors = LocalAppColors.current
    val min = modifier.min
    val max = modifier.max
    selectedModifiers.clear()
    val selectedItems =
        remember { mutableStateListOf<MenuModifierItem>(*defaultSelectedItems.toTypedArray()) }
    selectedItems.forEach { modifierItem ->
        selectedModifiers.add(
            ModifierItemOrderProduct(
                internalId = homeScreenViewModel.getNewId(),
                parentId = modifier.modifiers_id,
                NameParent = modifier.title,
                modifier_item_name = modifierItem.name,
                modifier_item_Price = modifierItem.price,
                modifier_item_id = modifierItem.modifiers_item_id,
                number = 1
            )
        )
    }
    homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                1.dp,
                color = colors.productDetailsColorLighter,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
    ) {
        Column {
            Row(modifier = Modifier.padding(3.dp)) {
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.padding(3.dp)) {
                Text(
                    text = modifier.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.productDetailsColorLighter
                )
                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(1.dp))

                val annotatedType = buildAnnotatedString {
                    append("النوع: \n")
                    withStyle(
                        style = SpanStyle(
                            background = colors.productDetailsColorLighter,
                            color = white
                        )
                    ) {

                        append(" اختياري ")

                    }
                }
                Spacer(modifier = Modifier.width(1.dp))

                val annotatedMin = buildAnnotatedString {
                    append("الحد الادنى: \n")
                    withStyle(
                        style = SpanStyle(
                            background = colors.productDetailsColorLighter,
                            color = white
                        )
                    ) {
                        append(" ${modifier.min} ")
                    }
                }
                Spacer(modifier = Modifier.width(1.dp))

                val annotatedMax = buildAnnotatedString {
                    append("الحد الأقصى: \n")
                    withStyle(
                        style = SpanStyle(
                            background = colors.productDetailsColorLighter,
                            color = white
                        )
                    ) {
                        append(" ${modifier.max} ")
                    }
                }
                Text(
                    text = annotatedMin,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = annotatedMax,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = annotatedType,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )


            }
            modifier.items.forEach { modifierItem ->
                val isSelected = selectedItems.contains(modifierItem)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {


                        Text(
                            text = modifierItem.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = priceText(modifierItem.price),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Checkbox(
                        colors = CheckboxDefaults.colors(checkedColor = colors.productDetailsColorLighter),
                        checked = isSelected,
                        onCheckedChange = { checked ->
                            if (checked) {
                                // لو العدد الحالي أقل من max فقط اسمح بالاختيار
                                if (selectedItems.size < max) {

                                    selectedItems.add(modifierItem)
                                    onSelectedItemChange(
                                        selectedItem.copy()
                                    )

                                    selectedModifiers.clear()

                                    selectedItems.forEach { modifierItem ->
                                        selectedModifiers.add(
                                            ModifierItemOrderProduct(
                                                internalId = homeScreenViewModel.getNewId(),
                                                parentId = modifier.modifiers_id,
                                                NameParent = modifier.title,
                                                modifier_item_name = modifierItem.name,
                                                modifier_item_Price = modifierItem.price,
                                                modifier_item_id = modifierItem.modifiers_item_id,
                                                number = 1
                                            )
                                        )
                                    }
                                    homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
                                }
                            } else {

                                if (selectedItems.size > min) {
                                    selectedModifiers.clear()

                                    selectedItems.remove(modifierItem)
                                    onSelectedItemChange(
                                        selectedItem.copy( )
                                    )
                                    selectedItems.forEach { modifierItem ->
                                        selectedModifiers.add(
                                            ModifierItemOrderProduct(
                                                internalId = homeScreenViewModel.getNewId(),
                                                parentId = modifier.modifiers_id,
                                                NameParent = modifier.title,
                                                modifier_item_name = modifierItem.name,
                                                modifier_item_Price = modifierItem.price,
                                                modifier_item_id = modifierItem.modifiers_item_id,
                                                number = 1
                                            )
                                        )
                                    }
                                    homeScreenViewModel.setTotalPrice( sumTotalItemPrice(selectedItem))
                                }
                            }
                        }
                    )
                }
            }

        }
    }
}
 fun sumTotalItemPrice(orderItem :OrderItem): Double {

     var modifiersPrice :Double =0.0

     orderItem.modifiers.forEach { 
         modifier ->
         modifier.items.forEach { 
             modifierItem ->
             modifiersPrice += modifiersPrice 
         }
     }
    return (modifiersPrice+ orderItem.size_Price) * orderItem.count

 }