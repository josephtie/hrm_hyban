# StorageLocationControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**all**](#all) | **GET** /api/storage-locations | |
|[**create1**](#create1) | **POST** /api/storage-locations | |

# **all**
> Array<StorageLocation> all()


### Example

```typescript
import {
    StorageLocationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StorageLocationControllerApi(configuration);

const { status, data } = await apiInstance.all();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<StorageLocation>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create1**
> StorageLocation create1(storageLocation)


### Example

```typescript
import {
    StorageLocationControllerApi,
    Configuration,
    StorageLocation
} from './api';

const configuration = new Configuration();
const apiInstance = new StorageLocationControllerApi(configuration);

let storageLocation: StorageLocation; //

const { status, data } = await apiInstance.create1(
    storageLocation
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **storageLocation** | **StorageLocation**|  | |


### Return type

**StorageLocation**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

