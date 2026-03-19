# SanctionPersonnelRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteSanctionPersonnel**](#deletesanctionpersonnel) | **POST** /api/rh/carriere/sanctions-personnel/supprimer | |
|[**findSanctionPersonnel**](#findsanctionpersonnel) | **POST** /api/rh/carriere/sanctions-personnel/trouver | |
|[**findSanctionPersonnels**](#findsanctionpersonnels) | **POST** /api/rh/carriere/sanctions-personnel/lister | |
|[**findSanctionPersonnelsByPersonnel**](#findsanctionpersonnelsbypersonnel) | **POST** /api/rh/carriere/sanctions-personnel/lister-par-personnel | |
|[**findSanctionPersonnelsBySanction**](#findsanctionpersonnelsbysanction) | **POST** /api/rh/carriere/sanctions-personnel/lister-par-sanction | |
|[**getSanctionPersonnelList**](#getsanctionpersonnellist) | **GET** /api/rh/carriere/sanctions-personnel/list | |
|[**saveSanctionPersonnel**](#savesanctionpersonnel) | **POST** /api/rh/carriere/sanctions-personnel/enregistrer | |
|[**viewSanctionsPersonnel**](#viewsanctionspersonnel) | **GET** /api/rh/carriere/sanctions-personnel/view | |

# **deleteSanctionPersonnel**
> SanctionPersonnelDTO deleteSanctionPersonnel(sanctionPersonnelRequest)


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration,
    SanctionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

let sanctionPersonnelRequest: SanctionPersonnelRequest; //

const { status, data } = await apiInstance.deleteSanctionPersonnel(
    sanctionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionPersonnelRequest** | **SanctionPersonnelRequest**|  | |


### Return type

**SanctionPersonnelDTO**

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

# **findSanctionPersonnel**
> SanctionPersonnelDTO findSanctionPersonnel(sanctionPersonnelRequest)


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration,
    SanctionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

let sanctionPersonnelRequest: SanctionPersonnelRequest; //

const { status, data } = await apiInstance.findSanctionPersonnel(
    sanctionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionPersonnelRequest** | **SanctionPersonnelRequest**|  | |


### Return type

**SanctionPersonnelDTO**

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

# **findSanctionPersonnels**
> SanctionPersonnelDTO findSanctionPersonnels()


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.findSanctionPersonnels();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**SanctionPersonnelDTO**

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

# **findSanctionPersonnelsByPersonnel**
> SanctionPersonnelDTO findSanctionPersonnelsByPersonnel(sanctionPersonnelRequest)


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration,
    SanctionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

let sanctionPersonnelRequest: SanctionPersonnelRequest; //

const { status, data } = await apiInstance.findSanctionPersonnelsByPersonnel(
    sanctionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionPersonnelRequest** | **SanctionPersonnelRequest**|  | |


### Return type

**SanctionPersonnelDTO**

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

# **findSanctionPersonnelsBySanction**
> SanctionPersonnelDTO findSanctionPersonnelsBySanction(sanctionPersonnelRequest)


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration,
    SanctionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

let sanctionPersonnelRequest: SanctionPersonnelRequest; //

const { status, data } = await apiInstance.findSanctionPersonnelsBySanction(
    sanctionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionPersonnelRequest** | **SanctionPersonnelRequest**|  | |


### Return type

**SanctionPersonnelDTO**

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

# **getSanctionPersonnelList**
> SanctionPersonnelResponseObject getSanctionPersonnelList()


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getSanctionPersonnelList(
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

**SanctionPersonnelResponseObject**

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

# **saveSanctionPersonnel**
> SanctionPersonnelDTO saveSanctionPersonnel(sanctionPersonnelRequest)


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration,
    SanctionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

let sanctionPersonnelRequest: SanctionPersonnelRequest; //

const { status, data } = await apiInstance.saveSanctionPersonnel(
    sanctionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sanctionPersonnelRequest** | **SanctionPersonnelRequest**|  | |


### Return type

**SanctionPersonnelDTO**

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

# **viewSanctionsPersonnel**
> string viewSanctionsPersonnel()


### Example

```typescript
import {
    SanctionPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SanctionPersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.viewSanctionsPersonnel();
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

