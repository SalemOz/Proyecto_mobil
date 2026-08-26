# 📚 Plan de Estudio — Proyecto Mockup Android

## 1. Estructura del Proyecto

```
app/
├── build.gradle.kts          ← Configuración del módulo (SDK, dependencias)
├── src/main/
│   ├── AndroidManifest.xml   ← Manifiesto de la app
│   ├── java/com/example/mockup/
│   │   └── MainActivity.kt   ← Actividad principal
│   └── res/
│       ├── drawable/          ← Imágenes y gráficos (vectores XML + PNGs)
│       ├── layout/
│       │   └── activity_main.xml  ← Diseño de la pantalla principal
│       ├── values/
│       │   ├── colors.xml    ← Paleta de colores
│       │   ├── strings.xml   ← Textos de la app
│       │   └── themes.xml    ← Temas y estilos
│       └── xml/               ← Reglas de backup
```

---

## 2. Validación de Nombre con Regex

### 2.1. ¿Qué es un Regex?

Un **regex** (expresión regular) es un patrón de texto que se usa para validar, buscar o reemplazar cadenas de caracteres.

### 2.2. El Regex Implementado

```kotlin
val nameRegex = "^[\p{L}][\p{L} '\-]{0,49}$".toRegex()
```

**Desglose del patrón:**

| Parte | Significado |
|-------|-------------|
| `^` | Inicio del string |
| `[\p{L}]` | Una letra Unicode (letra con acento, ñ, etc.) — **requerida como primer carácter** |
| `[\p{L} '\-]{0,49}` | De 0 a 49 caracteres más: letras, espacios, apóstrofes o guiones |
| `$` | Fin del string |

**¿Por qué `\p{L}` y no solo `a-z`?**
- `\p{L}` es una propiedad Unicode que incluye **todas las letras del mundo**: acentos (á, é, ñ), letras cirílicas, árabes, etc.
- Si usáramos solo `[a-zA-Z]`, nombres como "María" o "José" serían rechazados.

**¿Por qué `\-`?**
- El guión `-` dentro de un conjunto `[]` tiene un significado especial (rango, como `a-z`).
- Escaparlo con `\` lo convierte en un guión literal.

### 2.3. Validación de Caracteres Repetidos

```kotlin
val consecutiveRegex = "(.)\1".toRegex()
```

| Parte | Significado |
|-------|-------------|
| `(.)` | Captura cualquier carácter en un grupo |
| `\1` | Hace referencia al mismo carácter capturado |

**Ejemplo:** `"Mariía"` → detecta que `i` está repetido → error.

**¿Por qué se eliminan espacios/guiones/apóstrofes antes?**
Para que "María José" (válido) no se confunda con un error de repetición.

### 2.4. Nombres Reservados

```kotlin
val reservedNames = listOf("admin", "test", "usuario", "user", "root", "prueba")
```

Lista de nombres que no se permiten por razones de seguridad/buenas prácticas.

---

## 3. Manejo de Errores en Android

### 3.1. `EditText.error`

```kotlin
etNombre.error = "Mensaje de error"
```

Muestra un mensaje de error en rojo debajo del campo de texto. Se limpia automáticamente cuando el usuario escribe de nuevo.

### 3.2. `return@setOnClickListener`

```kotlin
if (name.isEmpty()) {
    etNombre.error = "Por favor, ingresa tu nombre"
    return@setOnClickListener  // ← Sale del listener, NO de la función
}
```

- `return` solo sale de la función actual.
- `return@setOnClickListener` sale del **lambda** del listener, deteniendo la ejecución del botón.
- Es necesario porque dentro de un listener, `return` simple causaría un error de compilación.

---

## 4. Manejo de Imágenes en Android

### 4.1. Vectores XML vs PNG

| Formato | Ventajas | Desventajas |
|---------|----------|-------------|
| **Vector XML** | Escala perfectamente, peso mínimo | Solo sirve para formas simples (iconos) |
| **PNG** | Cualquier imagen compleja | Pesa más, necesita múltiples resoluciones |

### 4.2. Carpetas de Densidad

Android usa diferentes densidades de pantalla:

```
drawable-mdpi/     → ~48×48 px  (densidad media)
drawable-hdpi/     → ~72×72 px  (densidad alta)
drawable-xhdpi/    → ~96×96 px  (densidad extra-alta)
drawable-xxhdpi/   → ~144×144 px
drawable-xxxhdpi/  → ~192×192 px
```

Si solo pones una resolución en `drawable/`, Android la escala automáticamente, pero puede verse borrosa o pixelada.

### 4.3. Nombres de Recursos

- Deben ser **minúsculas** (`ear_left`, no `Ear_left`)
- Sin espacios ni guiones (usar `_`)
- Sin extensión al usar en código: `@drawable/ear_left`

---

## 5. Referencias de Drawables en el Layout

```xml
<!-- Vector XML -->
android:src="@drawable/ear_sound"

<!-- PNG (misma sintaxis) -->
android:src="@drawable/ear_left"
```

**En Kotlin:**
```kotlin
imageView.setImageResource(R.drawable.ear_left)
```

### 5.1. `scaleX="-1"` (Voltear imagen)

```xml
android:scaleX="-1"
```

- Un valor de `-1` invierte la imagen horizontalmente (efecto espejo).
- Se usaba para que la oreja izquierda fuera el reflejo de la derecha.
- **Ya no se necesita** porque ahora tenemos PNGs separados para cada oreja.

---

## 6. Interacción con el Usuario

### 6.1. Selección de Oreja

El patrón de selección sigue la lógica:

```
1. Usuario toca una oreja
2. Se aplica fondo de selección (bg_ear_card_selected)
3. Se cambia el color del icono y texto (primary_blue)
4. Se muestra el check (visibility = VISIBLE)
5. La otra oreja se deselecciona
```

### 6.2. `Toast` (Mensaje temporal)

```kotlin
Toast.makeText(this, "Hola $name", Toast.LENGTH_LONG).show()
```

Muestra un mensaje flotante que desaparece solo después de unos segundos.

---

## 7. Resumen de Cambios Realizados

| Cambio | Archivo | Descripción |
|--------|---------|-------------|
| Validación de nombre | `MainActivity.kt` | Regex + repetidos + reservados |
| Imágenes de orejas | `drawable/` | `Ear_left.png` → `ear_left.png`, `Ear_right.png` → `ear_right.png` |
| Layout orejas | `activity_main.xml` | `ear_sound` → `ear_left`/`ear_right`, eliminado `scaleX` |
| Limpieza | `drawable/` | `ear_sound.xml` ya no se usa (pendiente eliminar) |
