#!/bin/bash

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}============================================${NC}"
echo -e "${BLUE}  Playwright Test Execution Script${NC}"
echo -e "${BLUE}============================================${NC}\n"

# Default values
BROWSER="chromium"
HEADLESS="false"
WORKERS="4"
RETRIES="2"
SCREENSHOT_FAILURE="true"
SCREENSHOT_SUCCESS="false"
PROFILE="default"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -b|--browser)
            BROWSER="$2"
            shift 2
            ;;
        -h|--headless)
            HEADLESS="true"
            shift
            ;;
        -w|--workers)
            WORKERS="$2"
            shift 2
            ;;
        -r|--retries)
            RETRIES="$2"
            shift 2
            ;;
        -p|--profile)
            PROFILE="$2"
            shift 2
            ;;
        --screenshot-all)
            SCREENSHOT_SUCCESS="true"
            shift
            ;;
        --help)
            echo "Usage: ./run-tests.sh [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  -b, --browser <name>     Browser to use (chromium, firefox, webkit) [default: chromium]"
            echo "  -h, --headless           Run in headless mode [default: false]"
            echo "  -w, --workers <number>   Number of parallel workers [default: 4]"
            echo "  -r, --retries <number>   Number of retries for failed tests [default: 2]"
            echo "  -p, --profile <name>     Maven profile to use (default, ci, firefox, webkit)"
            echo "  --screenshot-all         Take screenshots for passed tests too"
            echo "  --help                   Display this help message"
            echo ""
            echo "Examples:"
            echo "  ./run-tests.sh -b firefox -w 2"
            echo "  ./run-tests.sh --headless -p ci"
            echo "  ./run-tests.sh -b webkit --screenshot-all"
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

echo -e "${YELLOW}Configuration:${NC}"
echo -e "  Browser: ${GREEN}$BROWSER${NC}"
echo -e "  Headless: ${GREEN}$HEADLESS${NC}"
echo -e "  Workers: ${GREEN}$WORKERS${NC}"
echo -e "  Retries: ${GREEN}$RETRIES${NC}"
echo -e "  Profile: ${GREEN}$PROFILE${NC}"
echo -e "  Screenshot on Failure: ${GREEN}$SCREENSHOT_FAILURE${NC}"
echo -e "  Screenshot on Success: ${GREEN}$SCREENSHOT_SUCCESS${NC}\n"

# Run tests
echo -e "${BLUE}Running tests...${NC}\n"

mvn clean test -P$PROFILE \
    -Dbrowser=$BROWSER \
    -Dheadless=$HEADLESS \
    -Dworkers=$WORKERS \
    -Dretries=$RETRIES \
    -Dscreenshot.failure=$SCREENSHOT_FAILURE \
    -Dscreenshot.success=$SCREENSHOT_SUCCESS

# Check exit code
if [ $? -eq 0 ]; then
    echo -e "\n${GREEN}✓ Tests completed successfully!${NC}"
else
    echo -e "\n${RED}✗ Tests failed!${NC}"
    exit 1
fi
