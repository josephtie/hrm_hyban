# PosteControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePoste4**](#deleteposte4) | **POST** /carriere/supprimerposte | |
|[**findPoste2**](#findposte2) | **POST** /carriere/trouverposte | |
|[**findPostes4**](#findpostes4) | **POST** /carriere/listerpostes | |
|[**getUserListJSON10**](#getuserlistjson10) | **GET** /carriere/paginerpostes | |
|[**savePoste**](#saveposte) | **POST** /carriere/enregistrerposte | |

# **deletePoste4**
> PosteDTO deletePoste4()


### Example

```typescript
import {
    PosteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PosteControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.deletePoste4(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**PosteDTO**

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

# **findPoste2**
> PosteDTO findPoste2()


### Example

```typescript
import {
    PosteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PosteControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.findPoste2(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**PosteDTO**

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

# **findPostes4**
> PosteDTO findPostes4()


### Example

```typescript
import {
    PosteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PosteControllerApi(configuration);

const { status, data } = await apiInstance.findPostes4();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PosteDTO**

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

# **getUserListJSON10**
> PosteDTO getUserListJSON10()


### Example

```typescript
import {
    PosteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PosteControllerApi(configuration);

let limit: number; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getUserListJSON10(
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **limit** | [**number**] |  | (optional) defaults to undefined|
| **offset** | [**number**] |  | (optional) defaults to undefined|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PosteDTO**

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

# **savePoste**
> PosteDTO savePoste()


### Example

```typescript
import {
    PosteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PosteControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let libelle: string; // (optional) (default to undefined)
let description: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.savePoste(
    id,
    libelle,
    description
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **libelle** | [**string**] |  | (optional) defaults to undefined|
| **description** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PosteDTO**

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

