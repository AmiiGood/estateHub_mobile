package com.oscar.estatehubcompose.properties.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.oscar.estatehubcompose.properties.data.network.response.Propiedad
import com.oscar.estatehubcompose.ui.theme.*
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Property(modifier: Modifier, propertyViewModel: PropertyViewModel, navController: androidx.navigation.NavController? = null) {

    val permisos = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        if (!permisos.allPermissionsGranted) {
            permisos.launchMultiplePermissionRequest()
        }
    }

    val propiedades by propertyViewModel.propiedades.observeAsState(emptyList())
    val isLoading by propertyViewModel.isLoading.observeAsState(false)
    val error by propertyViewModel.error.observeAsState()
    val selectedCategory by propertyViewModel.selectedCategory.observeAsState("Todo")
    val currentFilters by propertyViewModel.currentFilters.observeAsState(PropertyFilters())

    var showFilterDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isRefreshing = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            error != null -> {
                ErrorView(
                    error = error ?: "Error desconocido",
                    onRetry = { propertyViewModel.loadPropiedades() }
                )
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        propertyViewModel.refreshPropiedades()
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    propertyContent(
                        propiedades = propiedades,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { propertyViewModel.updateCategory(it) },
                        onSearch = { query -> propertyViewModel.searchByLocation(query) },
                        onFilterClick = { showFilterDialog = true },
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            currentFilters = currentFilters,
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { filters ->
                propertyViewModel.applyFilters(filters)
                showFilterDialog = false
            },
            onClearFilters = {
                propertyViewModel.clearFilters()
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun propertyContent(
    propiedades: List<Propiedad>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFilterClick: () -> Unit,
    isLoading: Boolean = false
) {
    var searchText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SearchBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onFilterClick = onFilterClick,
                onSearch = { onSearch(searchText) }
            )
        }

        item {
            CategoryRow(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        if (isLoading && propiedades.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = PrimaryPersonalized,
                        strokeWidth = 2.dp
                    )
                }
            }
        }

        items(propiedades) { propiedad ->
            PropertyCard(propiedad = propiedad)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onSearch: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                modifier = Modifier.size(24.dp),
                tint = PrimaryPersonalized
            )

            Spacer(modifier = Modifier.width(12.dp))

            androidx.compose.foundation.text.BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Normal,
                    color = PrimaryPersonalized
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (searchText.isEmpty()) {
                        Text(
                            text = "Buscar por ciudad o estado...",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.Normal,
                                color = SeventhPersonalized
                            )
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch() }
                )
            )

            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchTextChange("")
                        onSearch()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        modifier = Modifier.size(20.dp),
                        tint = SeventhPersonalized
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, ThirdPersonalized),
                modifier = Modifier.clickable(onClick = onFilterClick)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filtros",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp),
                    tint = PrimaryPersonalized
                )
            }
        }
    }
}

@Composable
fun CategoryRow(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        CategoryItem("Todo", Icons.Outlined.Home),
        CategoryItem("Casas", Icons.Outlined.House),
        CategoryItem("Cuartos", Icons.Outlined.Apartment),
        CategoryItem("En venta", Icons.Outlined.Sell),
        CategoryItem("En renta", Icons.Outlined.Key)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategory == category.name,
                onClick = { onCategorySelected(category.name) }
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(top = 8.dp),
        thickness = 1.dp,
        color = ThirdPersonalized
    )
}

@Composable
fun CategoryChip(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) ThirdPersonalized else Color.Transparent,
        border = if (isSelected)
            BorderStroke(1.dp, PrimaryPersonalized)
        else
            BorderStroke(1.dp, Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) PrimaryPersonalized else SeventhPersonalized
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = Parkinsans,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) PrimaryPersonalized else SeventhPersonalized
                )
            )
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PropertyCard(propiedad: Propiedad) {
    val pagerState = rememberPagerState()
    val imageCount = propiedad.imagenes.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { /* Navegar a detalles */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            if (imageCount > 0) {
                HorizontalPager(
                    count = imageCount,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = propiedad.imagenes[page].urlImagen,
                        contentDescription = propiedad.titulo,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                if (imageCount > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(imageCount) { index ->
                            Surface(
                                modifier = Modifier.size(6.dp),
                                shape = CircleShape,
                                color = if (index == pagerState.currentPage)
                                    Color.White
                                else
                                    Color.White.copy(alpha = 0.5f)
                            ) {}
                        }
                    }
                }

                if (propiedad.estatus != "disponible") {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = PrimaryPersonalized.copy(alpha = 0.85f)
                    ) {
                        Text(
                            text = propiedad.estatus.capitalize(Locale.getDefault()),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }

                if (imageCount > 1) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = PrimaryPersonalized.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "${pagerState.currentPage + 1}/$imageCount",
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${propiedad.ciudad}, ${propiedad.estado}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryPersonalized
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = propiedad.tipoPropiedad.capitalize(Locale.getDefault()),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Normal,
                        color = SeventhPersonalized
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${propiedad.numHabitaciones} hab · ${propiedad.numBanios} baños · ${propiedad.metrosCuadrados.toInt()} m²",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Normal,
                        color = SeventhPersonalized
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formatPriceCompact(propiedad.precioVenta ?: propiedad.precioRenta ?: 0.0),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryPersonalized
                        )
                    )
                    Text(
                        text = if (propiedad.precioRenta != null && propiedad.precioRenta > 0) " / mes" else "",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Normal,
                            color = PrimaryPersonalized
                        )
                    )
                }
            }

            if (propiedad.credito) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ThirdPersonalized
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Crédito",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF008A05)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Crédito",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF008A05)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorView(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = SeventhPersonalized
            )

            Text(
                text = "Algo salió mal",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPersonalized
                )
            )

            Text(
                text = error,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Normal,
                    color = SeventhPersonalized
                )
            )

            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPersonalized
                )
            ) {
                Text(
                    "Reintentar",
                    style = TextStyle(
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun FilterDialog(
    currentFilters: PropertyFilters,
    onDismiss: () -> Unit,
    onApplyFilters: (PropertyFilters) -> Unit,
    onClearFilters: () -> Unit
) {
    var tipoPropiedad by remember { mutableStateOf(currentFilters.tipoPropiedad ?: "") }
    var precioMin by remember { mutableStateOf(currentFilters.precioMin?.toFloat() ?: 0f) }
    var precioMax by remember { mutableStateOf(currentFilters.precioMax?.toFloat() ?: 10000000f) }
    var habitaciones by remember { mutableStateOf(currentFilters.habitaciones ?: 0) }
    var banios by remember { mutableStateOf(currentFilters.banios ?: 0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ThirdPersonalized)
                        .padding(20.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = PrimaryPersonalized
                        )
                    }

                    Text(
                        text = "Filtros",
                        style = TextStyle(
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = PrimaryPersonalized
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )

                    TextButton(
                        onClick = onClearFilters,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            "Limpiar",
                            style = TextStyle(
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = SeventhPersonalized
                            )
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        Column {
                            Text(
                                "Tipo de propiedad",
                                style = TextStyle(
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = PrimaryPersonalized
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                FilterChip(
                                    label = "Residencial",
                                    isSelected = tipoPropiedad == "residencial",
                                    onClick = {
                                        tipoPropiedad = if (tipoPropiedad == "residencial") "" else "residencial"
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                FilterChip(
                                    label = "Departamento",
                                    isSelected = tipoPropiedad == "departamento",
                                    onClick = {
                                        tipoPropiedad = if (tipoPropiedad == "departamento") "" else "departamento"
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    item {
                        Column {
                            Text(
                                "Rango de precio",
                                style = TextStyle(
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = PrimaryPersonalized
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    formatPriceCompact(precioMin.toDouble()),
                                    style = TextStyle(
                                        fontFamily = Parkinsans,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = SeventhPersonalized
                                    )
                                )
                                Text(
                                    formatPriceCompact(precioMax.toDouble()),
                                    style = TextStyle(
                                        fontFamily = Parkinsans,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = SeventhPersonalized
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            RangeSlider(
                                value = precioMin..precioMax,
                                onValueChange = { range ->
                                    precioMin = range.start
                                    precioMax = range.endInclusive
                                },
                                valueRange = 0f..10000000f,
                                steps = 100,
                                colors = SliderDefaults.colors(
                                    thumbColor = PrimaryPersonalized,
                                    activeTrackColor = PrimaryPersonalized,
                                    inactiveTrackColor = ThirdPersonalized
                                )
                            )
                        }
                    }

                    item {
                        Column {
                            Text(
                                "Habitaciones",
                                style = TextStyle(
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = PrimaryPersonalized
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                NumberSelector(
                                    label = "Todos",
                                    isSelected = habitaciones == 0,
                                    onClick = { habitaciones = 0 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "1",
                                    isSelected = habitaciones == 1,
                                    onClick = { habitaciones = 1 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "2",
                                    isSelected = habitaciones == 2,
                                    onClick = { habitaciones = 2 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "3",
                                    isSelected = habitaciones == 3,
                                    onClick = { habitaciones = 3 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "4+",
                                    isSelected = habitaciones >= 4,
                                    onClick = { habitaciones = 4 },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    item {
                        Column {
                            Text(
                                "Baños",
                                style = TextStyle(
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = PrimaryPersonalized
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                NumberSelector(
                                    label = "Todos",
                                    isSelected = banios == 0,
                                    onClick = { banios = 0 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "1",
                                    isSelected = banios == 1,
                                    onClick = { banios = 1 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "2",
                                    isSelected = banios == 2,
                                    onClick = { banios = 2 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "3",
                                    isSelected = banios == 3,
                                    onClick = { banios = 3 },
                                    modifier = Modifier.weight(1f)
                                )
                                NumberSelector(
                                    label = "4+",
                                    isSelected = banios >= 4,
                                    onClick = { banios = 4 },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = {
                            onApplyFilters(
                                PropertyFilters(
                                    ciudad = null,
                                    estado = null,
                                    tipoPropiedad = tipoPropiedad.ifEmpty { null },
                                    precioMin = if (precioMin > 0) precioMin.toDouble() else null,
                                    precioMax = if (precioMax < 10000000f) precioMax.toDouble() else null,
                                    habitaciones = if (habitaciones > 0) habitaciones else null,
                                    banios = if (banios > 0) banios else null
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPersonalized
                        )
                    ) {
                        Text(
                            "Mostrar resultados",
                            style = TextStyle(
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PrimaryPersonalized else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) PrimaryPersonalized else ThirdPersonalized
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isSelected) Color.White else PrimaryPersonalized
                )
            )
        }
    }
}

@Composable
fun NumberSelector(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PrimaryPersonalized else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) PrimaryPersonalized else ThirdPersonalized
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isSelected) Color.White else PrimaryPersonalized
                )
            )
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector)

fun formatPriceCompact(price: Double): String {
    return when {
        price >= 1_000_000 -> {
            val millions = price / 1_000_000
            "$${String.format("%.1f", millions)}M"
        }
        price >= 1_000 -> {
            val thousands = price / 1_000
            "$${String.format("%.0f", thousands)}K"
        }
        else -> {
            "$${String.format("%.0f", price)}"
        }
    }
}