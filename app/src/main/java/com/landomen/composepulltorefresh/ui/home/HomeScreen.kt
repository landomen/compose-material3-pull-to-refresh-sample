package com.landomen.composepulltorefresh.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.landomen.composepulltorefresh.R
import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import com.landomen.composepulltorefresh.ui.theme.ComposePullToRefreshTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        HomeScreenContent(
            state = state,
            onRefreshTrigger = viewModel::onPullToRefreshTrigger,
            modifier = Modifier.padding(innerPadding)
        )
    }

}

@Composable
private fun HomeScreenContent(
    state: HomeScreenState,
    onRefreshTrigger: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    PullToRefreshWrapper(
        isRefreshing = state.isLoading,
        onRefreshTrigger = onRefreshTrigger,
        modifier = modifier
    ) {
        AnimalFactsList(state.items)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PullToRefreshWrapper(
    isRefreshing: Boolean,
    onRefreshTrigger: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val refreshState = rememberPullToRefreshState()

    // This essentially copies PullToRefreshBox since we can't use it directly
    // as it doesn't expose enabled parameter, which we need to disable pull-to-refresh
    // in offline mode
    Box(
        modifier.pullToRefresh(
            state = refreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefreshTrigger,
            enabled = enabled,
        ),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = refreshState,
        )
    }
}


@Composable
private fun AnimalFactsList(facts: List<AnimalFact>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(facts) { fact ->
            AnimalFactItem(fact = fact)
        }
    }
}

@Composable
private fun AnimalFactItem(fact: AnimalFact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = fact.fact,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalFactsListPreview() {
    ComposePullToRefreshTheme {
        AnimalFactsList(
            listOf(
                AnimalFact(1, "Fact 1"),
                AnimalFact(2, "Fact 2"),
                AnimalFact(3, "Fact 3"),
                AnimalFact(4, "Fact 4"),
                AnimalFact(5, "Fact 5"),
            )
        )
    }
}