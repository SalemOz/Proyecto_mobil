- **Yhoan Sebastian Tamayo Vargas**
- **Delia Del Pilar Valencia Conde**

# Oído+

App Android (Kotlin, minSdk 24) que redirige los sonidos del oído afectado al oído sano.

## Pantallas

| Pantalla | Archivo | Rol |
| --- | --- | --- |
| Onboarding | `activity_main.xml` + `MainActivity` | Nombre y oído afectado; se muestra solo la primera vez |
| Inicio | `activity_home.xml` + `HomeActivity` | Amplificación, volumen y modo de ambiente |
| Historial | `activity_historial.xml` + `HistorialActivity` | Sesiones de uso registradas |
| Ajustes | `activity_settings.xml` + `SettingsActivity` | Perfil y preferencias |

`BaseActivity` reúne lo que comparten las cuatro: barras del sistema en modo borde a borde,
insets y mensajes (Snackbar). `BottomNav` concentra la navegación entre las tres pantallas
principales, que incluyen el mismo `view_bottom_nav.xml`.

## Convenciones

### Ids

- **En inglés, `snake_case`, sin abreviaturas**, con prefijo por tipo de vista:
  `tv_` (TextView), `iv_` (ImageView), `et_` (EditText), `btn_` (botón o vista pulsable),
  `ll_` (LinearLayout), `fl_` (FrameLayout), `rv_` (RecyclerView), `rb_` (RadioButton),
  `switch_`, `slider_`, `view_` (View), `row_` (fila de ajustes), `nav_` (pestaña).
- El id describe **el rol**, no la pantalla: `tv_screen_title` (no `tv_title_settings`).
- Un id se repite en varios layouts **solo** si el componente es compartido
  (`view_bottom_nav.xml` define `nav_home`, `nav_history`, `nav_settings` una sola vez).
- Las vistas decorativas se marcan `contentDescription="@null"` +
  `importantForAccessibility="no"`; nunca se reutiliza la etiqueta de otra vista
  (el icono del menú anunciaba "Inicio").
- Los textos de vista previa van en `tools:text`; los reales, en `strings.xml`.
  En los layouts no se escribe `android:text` con `%s` (solo funcionaría formateado).

### Estilos y colores

- `values/colors.xml` es la única paleta, con nombres **semánticos** (`brand_primary`,
  `surface_card`, `text_secondary`, `outline`). Los layouts y drawables no escriben hex a mano
  ni usan nombres de color por pantalla (`settings_*`).
- `values/dimens.xml` define la escala de espaciado (`space_*`), los márgenes de pantalla y los
  radios. No se escriben medidas sueltas en los layouts.
- `values/styles.xml` define la tipografía (`Text.Oido.*`) y los componentes (`Widget.Oido.*`).
  Ojo: en Android el último punto de un nombre de estilo implica herencia, por eso existen
  `Text.Oido` y `Widget.Oido` vacíos como cabecera de familia.
- El estado seleccionado se resuelve con `isSelected` en el contenedor y selectores de color
  (`res/color/nav_item_color.xml`, `selectable_control_color.xml`) más `duplicateParentState`.
  No se pintan vistas a mano desde el código.
- Se usan `paddingStart/End/Top/Bottom` en lugar de `paddingHorizontal/Vertical`, que solo
  existen desde API 26 y el mínimo del proyecto es 24.
- La app es clara: el tema base es `Theme.Material3.Light` (no `DayNight`) y las barras del
  sistema usan siempre iconos oscuros.

### Estilo de código

`.editorconfig` fija UTF-8, saltos de línea LF, 4 espacios y salto final de archivo.

## Build

```bash
./gradlew :app:assembleDebug        # compilar
./gradlew :app:lintDebug            # análisis estático
./gradlew :app:testDebugUnitTest    # tests unitarios
```

En Windows, si `JAVA_HOME` está mal configurado, apuntarlo al JBR que trae Android Studio:

```bash
JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" ./gradlew :app:assembleDebug
```
