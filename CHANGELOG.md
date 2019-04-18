# Changelog
All notable changes to this project will be documented in this file.

## [Unreleased]


## [1.1.0] - 2019-04-18
### Changed
- Replace hardcoded build path with `$CI_PROJECT_DIR` Gitlab CI pre-defined env vars. 
- Update Integration test for Category based preference rebalancing stretch goal

### Added
- Add additional error handling for non-existant categories

## [1.0.0] - 2019-04-04
### Added
- MVP Endpoints completed
    - Create a new portfolio preference
    - Update an exisiting portfolio preference
    - Create a rebalance recommendation
    - Update transactions in a rebalance recommendation
    - Execute rebalance recommendation
- Stretch Goal Endpoints (Enhancements to MVP Endpoints)
    - Create a portfolio preference based on category
    - Update category based portfolio preference
    - Create a rebalance recommendation for categories
    - Provide ranked listed of recommended funds in a category
- Additional
    - Total balance endpoint to retrieve balance of customer
    - Gitlab CI/CD .gitlab-ci.yml file for automated deployment
