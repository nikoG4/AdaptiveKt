# Diseño de media/avatar en tablas y cards

## Roles de columna (AdaptiveDataMobileRole)
- Title
- Subtitle
- Metadata
- Status
- Media
- Actions
- Hidden

## Heurísticas
- avatar en tabla → avatar en card
- thumbnail de producto en tabla → thumbnail en card
- status badge
- metadata limitada en mobile
- si el usuario no configura roles, heurística por id/header
- evitar que la card sea una tabla vertical

## Ejemplo
- Employees: avatar, nombre, rol, status
- Products: thumbnail, nombre, stock, status
- Invoices: sin media, solo metadata
