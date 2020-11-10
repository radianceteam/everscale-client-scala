cmake_minimum_required(VERSION 3.17)
project(TonClientScalaBridge)

set(CMAKE_CXX_STANDARD 14)

include_directories(${CMAKE_SOURCE_DIR}/include)

if (DEFINED ENV{JAVA_HOME})
    string(REGEX REPLACE "\"" "" STRIPPED_JAVA_HOME $ENV{JAVA_HOME})
    string(CONCAT INCLUDE_PATH ${STRIPPED_JAVA_HOME} /include)
    file(TO_CMAKE_PATH ${INCLUDE_PATH} INCLUDE_NORMALIZED)
    message(WARNING "INCLUDE_NORMALIZED = ${INCLUDE_NORMALIZED}")
    if (WIN32)
        message(WARNING "Define operating system as Windows")
        string(CONCAT INCLUDE_SUBSYSTEM_PATH ${STRIPPED_JAVA_HOME} /include/win32)
        file(TO_CMAKE_PATH ${INCLUDE_SUBSYSTEM_PATH} INCLUDE_SUBSYSTEM_NORMALIZED)
        message(WARNING "INCLUDE_SUBSYSTEM_NORMALIZED = ${INCLUDE_SUBSYSTEM_NORMALIZED}")
    elseif(LINUX)
        message(WARNING "Define operating system as Unix")
        string(CONCAT INCLUDE_SUBSYSTEM_PATH ${STRIPPED_JAVA_HOME} /include/linux)
        file(TO_CMAKE_PATH ${INCLUDE_SUBSYSTEM_PATH} INCLUDE_SUBSYSTEM_NORMALIZED)
        message(WARNING "INCLUDE_SUBSYSTEM_NORMALIZED = ${INCLUDE_SUBSYSTEM_NORMALIZED}")
    else()
        message(FATAL_ERROR "Can't determine operation system")
    endif ()

else()
    message(FATAL_ERROR "Can't find JAVA_HOME environment variable.")
endif()

include_directories(${INCLUDE_NORMALIZED})

link_directories("${INCLUDE_NORMALIZED}")

include_directories(${INCLUDE_SUBSYSTEM_NORMALIZED})

link_directories(${INCLUDE_SUBSYSTEM_NORMALIZED})


if (WIN32)
    set(CMAKE_FIND_LIBRARY_SUFFIXES ".lib" ".dll")
endif()

find_library(ton_client_dll NAMES ton_client PATHS "${CMAKE_SOURCE_DIR}/../TON-SDK/ton_client/client/build")

if (ton_client_dll)
    add_library(ton_client_ref SHARED IMPORTED)
    set_property(TARGET ton_client_ref PROPERTY IMPORTED_LOCATION "${ton_client_dll}")
    add_library(TonClientScalaBridge SHARED src/bridge.cpp)
    target_link_libraries(TonClientScalaBridge ${ton_client_dll})
else()
    message(FATAL_ERROR "Can't find ton_sdk library")
endif()