package com.gotravel.gotravelina.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gotravel.gotravelina.data.Destination
import com.gotravel.gotravelina.di.Injection
import com.gotravel.gotravelina.ui.state.UiState
import com.gotravel.gotravelina.ui.item.DestinationItem
import com.gotravel.gotravelina.ui.item.Empty
import com.gotravel.gotravelina.ui.item.SearchButton
import com.gotravel.gotravelina.viewmodel.HomeViewModel
import com.gotravel.gotravelina.viewmodel.ViewModelFactory

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Int) -> Unit,
) {
    val query by viewModel.query.collectAsState()
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.search(query)
            }
            is UiState.Success -> {
                HomeContent(
                    query = query,
                    onQueryChange = viewModel::search,
                    listDestination = uiState.data,
                    onFavoriteIconClicked = { id, newState ->
                        viewModel.updateDestination(id, newState)
                    },
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    query: String,
    onQueryChange: (String) -> Unit,
    listDestination: List<Destination>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit
) {
    Column {
        SearchButton(
            query = query,
            onQueryChange = onQueryChange
        )
        if (listDestination.isNotEmpty()) {
            ListDestination(
                listDestination = listDestination,
                onFavoriteIconClicked = onFavoriteIconClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            Empty(
                Warning = "No Destination Found",
                modifier = Modifier
                    .testTag("EMPTY")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListDestination(
    listDestination: List<Destination>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    contentPaddingTop: Dp = 0.dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = contentPaddingTop
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .testTag("List")
    ) {
        items(listDestination, key = { it.id }) { items ->
            DestinationItem(
                id = items.id,
                name = items.name,
                location = items.location,
                image = items.image,
                rating = items.rating,
                isFavorite = items.isFavorite,
                onFavoriteIconClicked =  onFavoriteIconClicked,
                modifier = Modifier
                    .animateItemPlacement(tween(durationMillis = 0))
                    .clickable { navigateToDetail(items.id) }
            )
        }
    }
}
