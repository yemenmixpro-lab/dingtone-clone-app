package me.dingtone.app.im

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dingtone.app.im.ui.auth.AuthUiState
import me.dingtone.app.im.ui.auth.AuthViewModel
import me.dingtone.app.im.ui.theme.AccentGreen
import me.dingtone.app.im.ui.theme.BrandBlue
import me.dingtone.app.im.ui.theme.BrandSoftBlue
import me.dingtone.app.im.ui.theme.SurfaceLight
import me.dingtone.app.im.ui.theme.TextPrimary
import me.dingtone.app.im.ui.theme.TextSecondary

enum class AppScreen {
    LOGIN,
    SIGNUP,
    FORGOT_PASSWORD,
    HOME
}

@Composable
fun DingtoneApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.LOGIN) }
    val authViewModel: AuthViewModel = viewModel()

    when (currentScreen) {
        AppScreen.LOGIN -> LoginScreen(
            authViewModel = authViewModel,
            onLoginSuccess = { currentScreen = AppScreen.HOME },
            onCreateAccount = { currentScreen = AppScreen.SIGNUP },
            onForgotPassword = { currentScreen = AppScreen.FORGOT_PASSWORD }
        )
        AppScreen.SIGNUP -> SignUpScreen(
            authViewModel = authViewModel,
            onBackToLogin = { currentScreen = AppScreen.LOGIN },
            onRegisterSuccess = { currentScreen = AppScreen.HOME }
        )
        AppScreen.FORGOT_PASSWORD -> ForgotPasswordScreen(
            authViewModel = authViewModel,
            onBackToLogin = { currentScreen = AppScreen.LOGIN }
        )
        AppScreen.HOME -> HomeScreen(
            onLogout = {
                currentScreen = AppScreen.LOGIN
            }
        )
    }
}

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val state = authViewModel.authState

    if (state is AuthUiState.Success) {
        onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BrandBlue, BrandBlue.copy(alpha = 0.95f), SurfaceLight)
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(BrandSoftBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = BrandBlue)
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Column {
                        Text("Dingtone Clone", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("اتصال مجاني وموثوق", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }

                Text("تسجيل الدخول", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("البريد الإلكتروني") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("كلمة المرور") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (state is AuthUiState.Error) {
                    Text(state.message, color = Color.Red)
                }

                Button(
                    onClick = { authViewModel.signIn(email, password) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    enabled = state !is AuthUiState.Loading
                ) {
                    Text(if (state is AuthUiState.Loading) "جاري تسجيل الدخول..." else "تسجيل الدخول")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onForgotPassword) { Text("نسيت كلمة المرور؟") }
                    TextButton(onClick = onCreateAccount) { Text("إنشاء حساب") }
                }

                OutlinedButton(
                    onClick = { authViewModel.signIn(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    enabled = state !is AuthUiState.Loading
                ) {
                    Icon(Icons.Default.Security, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("متابعة مع الرقم")
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val state = authViewModel.authState

    if (state is AuthUiState.Success) {
        onRegisterSuccess()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(SurfaceLight).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = BrandBlue)
                    Text("إنشاء حساب جديد", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("الاسم الكامل") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("البريد الإلكتروني") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("كلمة المرور") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

                if (state is AuthUiState.Error) {
                    Text(state.message, color = Color.Red)
                }

                Button(
                    onClick = { authViewModel.signUp(email, password, name) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    enabled = state !is AuthUiState.Loading
                ) {
                    Text(if (state is AuthUiState.Loading) "جاري إنشاء الحساب..." else "إنشاء الحساب")
                }

                TextButton(onClick = onBackToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("العودة لتسجيل الدخول")
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    val state = authViewModel.authState

    Box(
        modifier = Modifier.fillMaxSize().background(SurfaceLight).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LockReset, contentDescription = null, tint = BrandBlue)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("استعادة كلمة المرور", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("البريد الإلكتروني") }, modifier = Modifier.fillMaxWidth())

                if (state is AuthUiState.Error) {
                    Text(state.message, color = Color.Red)
                }

                Button(
                    onClick = { authViewModel.resetPassword(email) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    enabled = state !is AuthUiState.Loading
                ) {
                    Text(if (state is AuthUiState.Loading) "جاري الإرسال..." else "إرسال رابط الاستعادة")
                }

                TextButton(onClick = onBackToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("العودة لتسجيل الدخول")
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val tabs = listOf(
        TabItem("المكالمات", Icons.Default.Call),
        TabItem("جهات الاتصال", Icons.Default.ContactPhone),
        TabItem("الرسائل", Icons.Default.ChatBubble),
        TabItem("الإعدادات", Icons.Default.Settings)
    )

    var selectedTab by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(SurfaceLight).padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> CallsTabContent(onLogout = onLogout)
                1 -> ContactsTabContent()
                2 -> MessagesTabContent()
                else -> SettingsTabContent(onLogout = onLogout)
            }
        }
    }
}

@Composable
fun CallsTabContent(onLogout: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { TopHeaderCard() }
        item { SearchBar() }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionCard("مكالمة صوتية", Icons.Default.Call, AccentGreen)
                QuickActionCard("مكالمة فيديو", Icons.Default.ContactPhone, BrandBlue)
            }
        }
        item { SectionTitle("سجل المكالمات") }
        items(
            listOf(
                CallEntry("أحمد", "الآن", "12:04 دقيقة"),
                CallEntry("سارة", "أمس", "8:32 دقيقة"),
                CallEntry("أميرة", "الاثنين", "4:16 دقيقة"),
                CallEntry("خالد", "الأحد", "18:10 دقيقة")
            )
        ) { call ->
            CallRow(call)
        }
        item { TextButton(onClick = onLogout) { Text("تسجيل الخروج") } }
    }
}

@Composable
fun ContactsTabContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("جهات الاتصال") }
        items(
            listOf(
                ContactItem("أحمد سالم", "+966 55 123 4567"),
                ContactItem("سارة محمد", "+966 50 987 6543"),
                ContactItem("أميرة علي", "+966 59 222 3344"),
                ContactItem("خالد يوسف", "+966 56 778 9900")
            )
        ) { item -> ContactRow(item) }
    }
}

@Composable
fun MessagesTabContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("الرسائل") }
        items(
            listOf(
                MessageItem("أحمد", "هل يمكننا الحديث الآن؟", "09:41"),
                MessageItem("سارة", "مارس العمل جاهز", "أمس"),
                MessageItem("أميرة", "شكرًا لك على المكالمة", "الأحد"),
                MessageItem("خالد", "سأرسل لك الملف", "السبت")
            )
        ) { item -> MessageRow(item) }
    }
}

@Composable
fun SettingsTabContent(onLogout: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionTitle("الإعدادات") }
        item { SettingsCard("الحساب", Icons.Default.Person) }
        item { SettingsCard("الخصوصية", Icons.Default.Security) }
        item { SettingsCard("التنبيهات", Icons.Default.CheckCircle) }
        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text("تسجيل الخروج")
            }
        }
    }
}

@Composable
fun TopHeaderCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BrandBlue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("عالمك الصوتي", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelLarge)
                Text("مكالمات مجانية عبر الإنترنت", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("3 مكالمات مجانية اليوم", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyLarge)
            }
            Box(
                modifier = Modifier.align(Alignment.TopEnd).size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun SearchBar() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
            Spacer(modifier = Modifier.size(10.dp))
            Text("ابحث عن اسم أو رقم", color = TextSecondary)
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
}

@Composable
fun QuickActionCard(title: String, icon: ImageVector, color: Color) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.size(width = 160.dp, height = 110.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }
            Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        }
    }
}

@Composable
fun CallRow(entry: CallEntry) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(BrandSoftBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue)
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(entry.name, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(entry.date, color = TextSecondary)
                }
            }
            Text(entry.duration, color = TextSecondary)
        }
    }
}

@Composable
fun ContactRow(item: ContactItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().clickable { }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(BrandSoftBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue)
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(item.name, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.phone, color = TextSecondary)
                }
            }
            Icon(Icons.Default.Call, contentDescription = null, tint = AccentGreen)
        }
    }
}

@Composable
fun MessageRow(item: MessageItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(BrandSoftBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ChatBubble, contentDescription = null, tint = BrandBlue)
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(item.sender, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.preview, color = TextSecondary)
                }
            }
            Text(item.time, color = TextSecondary)
        }
    }
}

@Composable
fun SettingsCard(title: String, icon: ImageVector) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(BrandSoftBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = BrandBlue)
            }
            Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        }
    }
}

data class TabItem(val title: String, val icon: ImageVector)
data class CallEntry(val name: String, val date: String, val duration: String)
data class ContactItem(val name: String, val phone: String)
data class MessageItem(val sender: String, val preview: String, val time: String)
