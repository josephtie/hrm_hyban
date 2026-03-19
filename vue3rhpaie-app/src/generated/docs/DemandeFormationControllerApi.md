# DemandeFormationControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePoste2**](#deleteposte2) | **POST** /formation/supprimerdemandeformation | |
|[**findPoste**](#findposte) | **POST** /formation/trouverdemandeformation | |
|[**findPostes2**](#findpostes2) | **POST** /formation/listerdemandeformations | |
|[**findPostesvaides**](#findpostesvaides) | **POST** /formation/listerdemandeformationsValide | |
|[**findformation**](#findformation) | **POST** /formation/validerdemandeformation | |
|[**getUserListJSON9**](#getuserlistjson9) | **GET** /formation/paginerdemandeformations | |
|[**getUserListlJSON**](#getuserlistljson) | **GET** /formation/paginerdemandeformationsValide | |
|[**refuserdemandeformation**](#refuserdemandeformation) | **POST** /formation/refuserdemandeformation | |
|[**saveDemandeFormation**](#savedemandeformation) | **POST** /formation/enregistrerdemandeformation | |

# **deletePoste2**
> DemandeFormationDTO deletePoste2()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.deletePoste2(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**DemandeFormationDTO**

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

# **findPoste**
> DemandeFormationDTO findPoste()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.findPoste(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**DemandeFormationDTO**

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

# **findPostes2**
> DemandeFormationDTO findPostes2()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

const { status, data } = await apiInstance.findPostes2();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**DemandeFormationDTO**

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

# **findPostesvaides**
> DemandeFormationDTO findPostesvaides()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

const { status, data } = await apiInstance.findPostesvaides();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**DemandeFormationDTO**

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

# **findformation**
> DemandeFormationDTO findformation()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let dateValide: string; // (optional) (default to undefined)
let jalios: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.findformation(
    id,
    dateValide,
    jalios
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **dateValide** | [**string**] |  | (optional) defaults to undefined|
| **jalios** | [**number**] |  | (optional) defaults to undefined|


### Return type

**DemandeFormationDTO**

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

# **getUserListJSON9**
> DemandeFormationDTO getUserListJSON9()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let limit: number; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getUserListJSON9(
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

**DemandeFormationDTO**

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

# **getUserListlJSON**
> DemandeFormationDTO getUserListlJSON()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let limit: number; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getUserListlJSON(
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

**DemandeFormationDTO**

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

# **refuserdemandeformation**
> DemandeFormationDTO refuserdemandeformation()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let dateValide: string; // (optional) (default to undefined)
let touche: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.refuserdemandeformation(
    id,
    dateValide,
    touche
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **dateValide** | [**string**] |  | (optional) defaults to undefined|
| **touche** | [**number**] |  | (optional) defaults to undefined|


### Return type

**DemandeFormationDTO**

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

# **saveDemandeFormation**
> DemandeFormationDTO saveDemandeFormation()


### Example

```typescript
import {
    DemandeFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DemandeFormationControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let idService: number; // (optional) (default to undefined)
let idAnnee: number; // (optional) (default to undefined)
let objet: string; // (optional) (default to undefined)
let commentaire: string; // (optional) (default to undefined)
let dateDemande: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.saveDemandeFormation(
    id,
    idService,
    idAnnee,
    objet,
    commentaire,
    dateDemande
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **idService** | [**number**] |  | (optional) defaults to undefined|
| **idAnnee** | [**number**] |  | (optional) defaults to undefined|
| **objet** | [**string**] |  | (optional) defaults to undefined|
| **commentaire** | [**string**] |  | (optional) defaults to undefined|
| **dateDemande** | [**string**] |  | (optional) defaults to undefined|


### Return type

**DemandeFormationDTO**

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

