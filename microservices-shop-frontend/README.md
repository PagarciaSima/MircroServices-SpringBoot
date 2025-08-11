# MicroservicesShopFrontend

## Running Tests

To run all unit tests, use:

```bash
ng test
To run tests with coverage report:

bash
Copiar
Editar
ng test --code-coverage
The coverage report will be generated inside the coverage/ folder. Open index.html in your browser to view it.

Generating Documentation with Compodoc
Make sure Compodoc is installed:

bash
Copiar
Editar
npm install --save-dev @compodoc/compodoc
Generate documentation:

bash
Copiar
Editar
npx compodoc -p tsconfig.json
Serve documentation locally on a different port (e.g., 9001):

bash
Copiar
Editar
npx compodoc -s --port 9001
Open your browser at http://localhost:9001 to see the docs.