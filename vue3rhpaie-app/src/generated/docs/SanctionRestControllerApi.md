# SanctionRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteSanction**](#deletesanction) | **POST** /api/rh/carriere/sanctions/supprimer | |
|[**findSanction**](#findsanction) | **POST** /api/rh/carriere/sanctions/trouver | |
|[**findSanctions**](#findsanctions) | **POST** /api/rh/carriere/sanctions/lister | |
|[**getSanctionList**](#getsanctionlist) | **GET** /api/rh/carriere/sanctions/list | |
|[**getSocietes1**](#getsocietes1) | **GET** /api/rh/carriere/sanctions/societes | |
|[**getTypesSanctions**](#gettypessanctions) | **GET** /api/rh/carriere/sanctions/types-sanctions | |
|[**saveSanction**](#savesanction) | **POST** /api/rh/carriere/sanctions/enregistrer | |
|[**viewSanctions**](#viewsanctions) | **GET** /api/rh/carriere/sanctions/view | |

# **deleteSanction**
> SanctionDTO deleteSanction(sanctionRequest)


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration,
    SanctionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

let sanctionRequest: SanctionRequest; //

const { status, data } = await apiInstance.deleteSanction(
    sanctionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionRequest** | **SanctionRequest**|  | |


### Return type

**SanctionDTO**

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

# **findSanction**
> SanctionDTO findSanction(sanctionRequest)


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration,
    SanctionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

let sanctionRequest: SanctionRequest; //

const { status, data } = await apiInstance.findSanction(
    sanctionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionRequest** | **SanctionRequest**|  | |


### Return type

**SanctionDTO**

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

# **findSanctions**
> SanctionDTO findSanctions()


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.findSanctions();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**SanctionDTO**

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

# **getSanctionList**
> SanctionResponseObject getSanctionList()


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getSanctionList(
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **limit** | [**number**] |  | (optional) defaults to 10|
| **offset** | [**number**] |  | (optional) defaults to 0|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**SanctionResponseObject**

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

# **getSocietes1**
> Array<Societe> getSocietes1()


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Societe>**

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

# **getTypesSanctions**
> object getTypesSanctions()


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.getTypesSanctions();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **saveSanction**
> SanctionDTO saveSanction(sanctionRequest)


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration,
    SanctionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

let sanctionRequest: SanctionRequest; //

const { status, data } = await apiInstance.saveSanction(
    sanctionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionRequest** | **SanctionRequest**|  | |


### Return type

**SanctionDTO**

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

# **viewSanctions**
> string viewSanctions()


### Example

```typescript
import {
    SanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.viewSanctions();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

