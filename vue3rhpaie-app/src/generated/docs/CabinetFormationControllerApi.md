# CabinetFormationControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePoste3**](#deleteposte3) | **POST** /formation/supprimercabinetformation | |
|[**findPoste1**](#findposte1) | **POST** /formation/trouvercabinetformation | |
|[**findPostes3**](#findpostes3) | **POST** /formation/listercabinetformations | |
|[**getUserListJSON4**](#getuserlistjson4) | **POST** /formation/paginercabinetformations | |
|[**saveCabinetFormation**](#savecabinetformation) | **POST** /formation/enregistrercabinetformation | |

# **deletePoste3**
> CabinetFormationDTO deletePoste3(idRequest)


### Example

```typescript
import {
    CabinetFormationControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CabinetFormationControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deletePoste3(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**CabinetFormationDTO**

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

# **findPoste1**
> CabinetFormationDTO findPoste1(idRequest)


### Example

```typescript
import {
    CabinetFormationControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CabinetFormationControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findPoste1(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**CabinetFormationDTO**

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

# **findPostes3**
> CabinetFormationDTO findPostes3()


### Example

```typescript
import {
    CabinetFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CabinetFormationControllerApi(configuration);

const { status, data } = await apiInstance.findPostes3();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**CabinetFormationDTO**

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

# **getUserListJSON4**
> CabinetFormationDTO getUserListJSON4(paginationRequest)


### Example

```typescript
import {
    CabinetFormationControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CabinetFormationControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON4(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**CabinetFormationDTO**

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

# **saveCabinetFormation**
> CabinetFormationDTO saveCabinetFormation(cabinetFormationRequest)


### Example

```typescript
import {
    CabinetFormationControllerApi,
    Configuration,
    CabinetFormationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CabinetFormationControllerApi(configuration);

let cabinetFormationRequest: CabinetFormationRequest; //

const { status, data } = await apiInstance.saveCabinetFormation(
    cabinetFormationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cabinetFormationRequest** | **CabinetFormationRequest**|  | |


### Return type

**CabinetFormationDTO**

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

