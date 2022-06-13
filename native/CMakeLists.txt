cmake_minimum_required(VERSION 3.10)
project(TonClientScalaBridge)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_INCLUDE_DIRECTORIES_BEFORE OFF)
include_directories(${CMAKE_SOURCE_DIR}/include)

find_package(JNI)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
else()
    message(FATAL_ERROR "Can't find JNI")
endif()

if (WIN32)
    set(CMAKE_FIND_LIBRARY_SUFFIXES ".dll.lib" ".dll")
elseif(CYGWIN)
    SET (CMAKE_FIND_LIBRARY_PREFIXES "")
    SET (CMAKE_FIND_LIBRARY_SUFFIXES ".lib" ".dll")
endif()

file(TO_CMAKE_PATH ${CMAKE_SOURCE_DIR}/../ever-sdk/ton_client/build PATH_TO_LIBRARY)
find_library(ton_client_dll NAMES ton_client PATHS ${PATH_TO_LIBRARY})
message(WARNING "${PATH_TO_LIBRARY}")
message(WARNING ${ton_client_dll})

if (ton_client_dll)
    add_library(ton_client_ref SHARED IMPORTED)
    set_property(TARGET ton_client_ref PROPERTY IMPORTED_LOCATION "${ton_client_dll}")
    add_library(TonClientScalaBridge SHARED src/bridge.cpp)

    target_include_directories(TonClientScalaBridge PUBLIC ${JNI_INCLUDE_DIRS})
    #target_include_directories(TonClientScalaBridge PUBLIC ${JNI_SUBSYSTEM_INCLUDE})
    target_link_libraries(TonClientScalaBridge ${ton_client_dll})

    set_target_properties(TonClientScalaBridge PROPERTIES OUTPUT_NAME "ton_client_scala_bridge")
else()
    message(FATAL_ERROR "Can't find ton_sdk library")
endif()