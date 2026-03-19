# FormationControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePoste1**](#deleteposte1) | **POST** /formation/supprimerformation | |
|[**findFormation**](#findformation) | **POST** /formation/trouverformation | |
|[**findPostes**](#findpostes) | **POST** /formation/listerformations | |
|[**getUserListJSON8**](#getuserlistjson8) | **GET** /formation/paginerformations | |
|[**saveFormation**](#saveformation) | **POST** /formation/enregistrerformation | |

# **deletePoste1**
> FormationDTO deletePoste1()


### Example

```typescript
import {
    FormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.deletePoste1(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**FormationDTO**

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

# **findFormation**
> FormationDTO findFormation()


### Example

```typescript
import {
    FormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.findFormation(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**FormationDTO**

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

# **findPostes**
> FormationDTO findPostes()


### Example

```typescript
import {
    FormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationControllerApi(configuration);

const { status, data } = await apiInstance.findPostes();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**FormationDTO**

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

# **getUserListJSON8**
> FormationDTO getUserListJSON8()


### Example

```typescript
import {
    FormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationControllerApi(configuration);

let limit: number; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getUserListJSON8(
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

**FormationDTO**

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

# **saveFormation**
> FormationDTO saveFormation()


### Example

```typescript
import {
    FormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let idTheme: number; // (optional) (default to undefined)
let idDemandeFormation: number; // (optional) (default to undefined)
let idCabinetFormation: number; // (optional) (default to undefined)
let intitule: string; // (optional) (default to undefined)
let dateDebut: string; // (optional) (default to undefined)
let dateFin: string; // (optional) (default to undefined)
let datePrevue: string; // (optional) (default to undefined)
let lieu: string; // (optional) (default to undefined)
let participant: number; // (optional) (default to undefined)
let planFormation: string; // (optional) (default to undefined)
let objectif: string; // (optional) (default to undefined)
let commentaire: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.saveFormation(
    id,
    idTheme,
    idDemandeFormation,
    idCabinetFormation,
    intitule,
    dateDebut,
    dateFin,
    datePrevue,
    lieu,
    participant,
    planFormation,
    objectif,
    commentaire
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **idTheme** | [**number**] |  | (optional) defaults to undefined|
| **idDemandeFormation** | [**number**] |  | (optional) defaults to undefined|
| **idCabinetFormation** | [**number**] |  | (optional) defaults to undefined|
| **intitule** | [**string**] |  | (optional) defaults to undefined|
| **dateDebut** | [**string**] |  | (optional) defaults to undefined|
| **dateFin** | [**string**] |  | (optional) defaults to undefined|
| **datePrevue** | [**string**] |  | (optional) defaults to undefined|
| **lieu** | [**string**] |  | (optional) defaults to undefined|
| **participant** | [**number**] |  | (optional) defaults to undefined|
| **planFormation** | [**string**] |  | (optional) defaults to undefined|
| **objectif** | [**string**] |  | (optional) defaults to undefined|
| **commentaire** | [**string**] |  | (optional) defaults to undefined|


### Return type

**FormationDTO**

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

