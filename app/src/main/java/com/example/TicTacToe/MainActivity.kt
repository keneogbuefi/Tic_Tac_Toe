package com.example.TicTacToe


import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.TicTacToe.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Tic Tac Toe", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PatternedBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text with a shadowed box
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Welcome to Tic Tac Toe!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("tictactoe?isHumanFirst=true") },
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Human First",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("tictactoe?isHumanFirst=false") },
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Machine First",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeScreen(navController: NavController, isHumanFirst: Boolean) {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf(if (isHumanFirst) "X" else "O") }
    var winner by remember { mutableStateOf<String?>(null) }
    var gameOver by remember { mutableStateOf(false) }
    var firstMoveMade by remember { mutableStateOf(false) }
    if (!firstMoveMade && !isHumanFirst) {
        board[2][0] = currentPlayer
        currentPlayer = "X"
        firstMoveMade = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tic Tac Toe",color = MaterialTheme.colorScheme.onSurface, fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        PatternedBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text with a shadowed box
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                Text(
                    text = "Turn: ${if (isHumanFirst) "Human" else "Machine"} First",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Board(
                board = board,
                onCellClick = { row, col ->
                    if (!gameOver && board[row][col].isEmpty() && currentPlayer == "X") {
                        board = board.mapIndexed { r, cols ->
                            cols.mapIndexed { c, value ->
                                if (r == row && c == col) currentPlayer else value
                            }.toMutableList()
                        }

                        if (checkWinner(board, currentPlayer)) {
                            winner = currentPlayer
                            gameOver = true
                        } else if (isBoardFull(board)) {
                            gameOver = true
                        } else {
                            currentPlayer = "O"

                            val handler: Handler = Handler()
                            handler.postDelayed(Runnable {
                                // Actions to do after 10 seconds
                                botMove(board,
                                    onBotPlayed = { newBoard ->
                                        board = newBoard as List<MutableList<String>>
                                        currentPlayer = "X"
                                    },
                                    gameOver = gameOver,
                                    onGameOver = { newBoard ->
                                        board = newBoard as List<MutableList<String>>
                                        winner = "O"
                                        gameOver = true
                                    }
                                )
                            }, 1000)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isBoardFull(board)) {
                currentPlayer = "X"
                gameOver = true
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.CenterHorizontally)
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    if (currentPlayer == "O") {
                        Row(Modifier.align(Alignment.CenterHorizontally)) {
                            LoadingSpinner()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (winner != null) {
                        currentPlayer = "X"
                        Text(
                            text = "Winner: $winner",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (winner == "X") Color(0xFFE5CB5F) else Color(0xF0F84747)
                        )
                    } else if (gameOver) {
                        currentPlayer = "X"
                        Text(
                            text = "Game Over! It's a draw.",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Current Turn: $currentPlayer",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                GameButton(text = "Go to Home") { navController.navigate("home") }
                GameButton(text = "Restart") {
                    board = List(3) { MutableList(3) { "" } }
                    currentPlayer = if (isHumanFirst) "X" else "O"
                    winner = null
                    firstMoveMade = false
                    gameOver = false
                }
            }
        }
    }
}

@Composable
fun GameButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun Board(board: List<List<String>>, onCellClick: (row: Int, col: Int) -> Unit) {
    Column {
        for (row in board.indices) {
            Row {
                for (col in board[row].indices) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(BorderStroke(2.dp, Color.Gray), RectangleShape)
                            .clickable { onCellClick(row, col) }
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            fontSize = 60.sp,
                            text = board[row][col],
                            color = if(board[row][col] == "X") Color(0xFFE5CB5F) else Color(0xF0F84747),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
        }
    }
}

// Helper functions remain unchanged






    fun checkWinner(board: List<List<String>>, currentPlayer: String): Boolean {
    // Check rows and columns
    for (i in 0..2) {
        if ((board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) ||
            (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)
        ) {
            return true
        }
    }

    // Check diagonals
    if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
        (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)
    ) {
        return true
    }

    return false
}

fun isBoardFull(board: List<List<String>>): Boolean {
    return board.all { row -> row.all { it.isNotEmpty() } }
}

fun botMove(
    board: List<List<String>>,
    onBotPlayed: (List<List<String>>) -> Unit,
    gameOver: Boolean,
    onGameOver: (Any?) -> Unit
) {
    if (gameOver) return


    val bestMove = findBestMove(board)
    val newBoard = board.mapIndexed { r, cols ->
        cols.mapIndexed { c, value ->
            if (r == bestMove.first && c == bestMove.second) "O" else value
        }.toMutableList()
    }
    Handler().postDelayed({
    if (checkWinner(newBoard, "O")) {
        onGameOver(newBoard)
    } else {
        onBotPlayed(newBoard)
    }
        }, 1000)
}

fun findBestMove(board: List<List<String>>): Pair<Int, Int> {
    var bestScore = Int.MIN_VALUE
    var bestMove = Pair(-1, -1)

    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j].isEmpty()) {
                val newBoard = board.map { it.toMutableList() }
                newBoard[i][j] = "O"
                val score = minimax(newBoard, depth = 0, isMaximizing = false)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = Pair(i, j)
                }
            }
        }
    }
    return bestMove
}

fun minimax(board: List<List<String>>, depth: Int, isMaximizing: Boolean): Int {
    val winner = when {
        checkWinner(board, "O") -> "O"
        checkWinner(board, "X") -> "X"
        isBoardFull(board) -> "draw"
        else -> null
    }

    if (winner != null) {
        return when (winner) {
            "O" -> 10 - depth
            "X" -> depth - 10
            "draw" -> 0
            else -> 0
        }
    }

    if (isMaximizing) {
        var bestScore = Int.MIN_VALUE
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].isEmpty()) {
                    val newBoard = board.map { it.toMutableList() }
                    newBoard[i][j] = "O"
                    val score = minimax(newBoard, depth + 1, isMaximizing = false)
                    bestScore = maxOf(score, bestScore)
                }
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].isEmpty()) {
                    val newBoard = board.map { it.toMutableList() }
                    newBoard[i][j] = "X"
                    val score = minimax(newBoard, depth + 1, isMaximizing = true)
                    bestScore = minOf(score, bestScore)
                }
            }
        }
        return bestScore
    }
}



@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable(
            route = "tictactoe?isHumanFirst={isHumanFirst}",
            arguments = listOf(navArgument("isHumanFirst") { type = NavType.BoolType })
        ) { backStackEntry ->
            TicTacToeScreen(
                navController = navController,
                isHumanFirst = backStackEntry.arguments?.getBoolean("isHumanFirst") ?: true
            )
        }
    }
}

fun DrawScope.drawXsAndOs(size: Int, spacing: Float, TextSize: TextUnit) {
    // Create a Paint object for drawing tex
    val paintX = Paint().apply {
        color = Color(0xFFE5CB5F).toArgb()
        textSize = TextSize.toPx() // Convert sp to px
        textAlign = Paint.Align.CENTER
    }

    val paintO = Paint().apply {
        color = Color(0xF0F84747).toArgb()
        textSize = TextSize.toPx()
        textAlign = Paint.Align.CENTER
    }

    for (i in 0 until size) {
        for (j in 0 until size) {
            var x = j * (spacing) + spacing / 2 -100// Center the X and O
            val y = i * (spacing) + spacing / 2 - 100
            if ((i) % 2 == 0) x -= spacing /4 -35
            // Draw X and O alternately
            val symbol = if ((i + j) % 2 == 0) "X" else "O"

            // Draw the symbol on the canvas
            drawContext.canvas.nativeCanvas.drawText(
                symbol,
                x,
                y,
                paintX.takeIf { symbol == "X" } ?: paintO // Select the appropriate paint
            )
        }
    }
}
@Composable
fun PatternedBackground() {
    val size = 30 // Number of symbols in each direction
    val spacing = 260f // Spacing between symbols
    val textSize = 50.sp // Size of the text

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawXsAndOs(size, spacing, textSize)
    }
}
@Composable
fun LoadingSpinner() {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
