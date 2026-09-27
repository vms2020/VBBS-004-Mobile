#!/usr/bin/env bash
# createfeature.sh — scaffold a new self-registering :feature:<name> module
#
# Usage:   ./mybin/createfeature.sh <Name>      # CamelCase, e.g. Shops, CurrencyRates
#
# Creates:
#   feature/<name>/build.gradle.kts
#   .../presentation/screens/<name>/<Name>Screen.kt      (hiltViewModel in body)
#   .../presentation/screens/<name>/<Name>ViewModel.kt
#   .../presentation/navigation/<Name>Entries.kt         (<name>EntryBuilder(navigator))
#   .../di/<Name>NavModule.kt                            (@IntoSet multibinding)
# Wires:
#   settings.gradle.kts      += include(":feature:<name>")
#   app/build.gradle.kts     += implementation(project(":feature:<name>"))
# Prints reminders for the two manual steps:
#   - Destination key in :core:navigation
#   - MenuItem in AppRoot's drawer

set -euo pipefail

NAME="${1:?Usage: createfeature.sh <Name> (CamelCase, e.g. Shops)}"
[[ "$NAME" =~ ^[A-Z][a-zA-Z0-9]*$ ]] || { echo "ERROR: '$NAME' must be CamelCase (e.g. Shops)"; exit 1; }

SNAKE="$(echo "$NAME" | sed 's/\([a-z0-9]\)\([A-Z]\)/\1_\2/g' | tr '[:upper:]' '[:lower:]')"
CAMEL="${NAME,}"                                  # lowerCamel: Shops -> shops, EditProfile -> editProfile
TITLE="$(echo "$NAME" | sed 's/\([a-z0-9]\)\([A-Z]\)/\1 \2/g')"

BASE="feature/$SNAKE"
PKG="io.bbs.seva.vbbs004mobile"
PKG_DIR="$BASE/src/main/java/$PKG"

[[ -e "$BASE" ]] && { echo "ERROR: $BASE already exists"; exit 1; }

mkdir -p "$PKG_DIR/presentation/screens/$SNAKE" \
         "$PKG_DIR/presentation/navigation" \
         "$PKG_DIR/di"

# ---------- build.gradle.kts ----------
cat > "$BASE/build.gradle.kts" <<EOF
plugins {
    id("vbbs.android.library.compose")
    id("vbbs.android.hilt")
}

android {
    namespace = "$PKG.feature.$SNAKE"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.hilt.navigation.compose)
}
EOF

# ---------- Screen ----------
cat > "$PKG_DIR/presentation/screens/$SNAKE/${NAME}Screen.kt" <<EOF
package $PKG.presentation.screens.$SNAKE

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ${NAME}Screen(
    modifier: Modifier = Modifier,
    viewModel: ${NAME}ViewModel = hiltViewModel(),
) {
    // val viewModel: ${NAME}ViewModel = hiltViewModel()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "$TITLE", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
EOF

# ---------- ViewModel ----------
cat > "$PKG_DIR/presentation/screens/$SNAKE/${NAME}ViewModel.kt" <<EOF
package $PKG.presentation.screens.$SNAKE

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ${NAME}ViewModel @Inject constructor() : ViewModel()
EOF

# ---------- Entries ----------
cat > "$PKG_DIR/presentation/navigation/${NAME}Entries.kt" <<EOF
package $PKG.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import $PKG.presentation.screens.$SNAKE.${NAME}Screen

fun EntryProviderScope<NavKey>.${CAMEL}EntryBuilder(navigator: AppNavigator) {
    entry<Destination.$NAME> {
        ${NAME}Screen()
    }
}
EOF

# ---------- NavModule ----------
cat > "$PKG_DIR/di/${NAME}NavModule.kt" <<EOF
package $PKG.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import $PKG.presentation.navigation.AppNavigator
import $PKG.presentation.navigation.${CAMEL}EntryBuilder

@Module
@InstallIn(ActivityRetainedComponent::class)
object ${NAME}NavModule {

    @IntoSet
    @Provides
    fun ${CAMEL}Entries(): EntryProviderScope<NavKey>.(AppNavigator) -> Unit = {
        ${CAMEL}EntryBuilder(it)
    }
}
EOF

# ---------- wiring (idempotent) ----------
grep -q "include(\":feature:$SNAKE\")" settings.gradle.kts || \
    echo "include(\":feature:$SNAKE\")" >> settings.gradle.kts

grep -q "feature:$SNAKE" app/build.gradle.kts || \
    sed -i "/^dependencies {/a implementation(project(\":feature:$SNAKE\"))" app/build.gradle.kts

# ---------- report ----------
echo ""
echo "Scaffolded :feature:$SNAKE"
echo "  $BASE/build.gradle.kts"
echo "  screens/$SNAKE: ${NAME}Screen.kt, ${NAME}ViewModel.kt"
echo "  navigation/${NAME}Entries.kt  (${CAMEL}EntryBuilder)"
echo "  di/${NAME}NavModule.kt        (${CAMEL}Entries multibinding)"
echo "  settings.gradle.kts, app deps: wired"
echo ""
echo "MANUAL STEPS REMAINING:"
echo "  1. core/navigation: add 'data object $NAME : Destination(...)'"
echo "     (match your Destination's actual shape — title, etc.)"
echo "  2. AppRoot: add MenuItem(Destination.$NAME, Icons....) to the drawer list"
echo "  3. If the feature needs extras (Coil, ktor, osmdroid...): add the"
echo "     implementation lines to $BASE/build.gradle.kts as the compiler demands"
echo ""
echo "THEN:"
echo "  ./gradlew :app:assembleDebug"
echo "  git add -A && git status --short | grep '/build/'   # must be empty"
echo "  git commit -m \"8.2-style: add :feature:$SNAKE\""
