# UI target
# Sauce Demo (e‑commerce): https://www.saucedemo.com
UI framework design:
- Browser creation is isolated in BrowserFactory
- Driver lifecycle is managed through lazy ThreadLocal DriverManager
- Page Objects encapsulate page behavior and locators
- BasePage provides explicit wait helpers
- TestNG listener captures screenshot and HTML only on failure
- Configuration is externalized through config.properties with system property override support
# API targetAAA Life  SDET Technical Assessment1
# Restful Booker: https://restful-booker.herokuapp.com
1) API Test Suite
   Implement positive and negative tests for at least 3 endpoints on the API
   Include at least one unhappy path and one boundary condition per endpoint
   Validate response contracts (for example, with JSON Schema or equivalent)
   Parameterize a subset of tests with external data CSV or JSON is fine)

Submission
Please submit a single zip file containing your work.
Include the following inside the zip:
All source code
A README with setup and run instructions, and any prerequisites
At least one local run output (test report or console log)
A brief Decision Log explaining:AAA Life  SDET Technical Assessment2
Scope interpretation and timeboxing
Test selection and coverage rationale
Stability and data strategies
Project structure decisions
Next steps if given more time