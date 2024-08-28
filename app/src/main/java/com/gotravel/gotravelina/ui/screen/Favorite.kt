package com.gotravel.gotravelina.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gotravel.gotravelina.R
import com.gotravel.gotravelina.data.Destination
import com.gotravel.gotravelina.di.Injection
import com.gotravel.gotravelina.ui.item.Empty
import com.gotravel.gotravelina.ui.state.UiState
import com.gotravel.gotravelina.viewmodel.FavoriteViewModel
import com.gotravel.gotravelina.viewmodel.ViewModelFactory

@Composable
fun Favorite(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getDestinationFavorite()
            }
            is UiState.Success -> {
                FavoriteInformation(
                    listDestination = uiState.data,
                    navigateToDetail = navigateToDetail,
                    onFavoriteIconClicked = { id, newState ->
                        viewModel.updateDestination(id, newState)
                    },
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun FavoriteInformation(
    listDestination: List<Destination>,
    navigateToDetail: (Int) -> Unit,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        if (listDestination.isNotEmpty()) {
            ListDestination(
                listDestination = listDestination,
                onFavoriteIconClicked = onFavoriteIconClicked,
                contentPaddingTop = 16.dp,
                navigateToDetail = navigateToDetail
            )
        } else {
            Empty(
                Warning = stringResource(R.string.no_favorite),
                modifier = modifier
            )
        }
    }
}