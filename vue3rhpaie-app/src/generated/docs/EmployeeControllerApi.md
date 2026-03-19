# EmployeeControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deactivate**](#deactivate) | **DELETE** /api/personnels/specifique/{id} | |
|[**get**](#get) | **GET** /api/personnels/specifique/{id} | |
|[**getPersonnelListJSON**](#getpersonnellistjson) | **GET** /api/personnels/specifique/listemployeejson | |
|[**list**](#list) | **GET** /api/personnels/specifique | |
|[**saveEmployeeWithContract**](#saveemployeewithcontract) | **POST** /api/personnels/specifique/enregisteremployee | |

# **deactivate**
> deactivate()


### Example

```typescript
import {
    EmployeeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deactivate(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get**
> Employee get()


### Example

```typescript
import {
    EmployeeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.get(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**Employee**

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

# **getPersonnelListJSON**
> EmployeeDTO getPersonnelListJSON()


### Example

```typescript
import {
    EmployeeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeControllerApi(configuration);

let limit: number; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPersonnelListJSON(
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

**EmployeeDTO**

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

# **list**
> Array<Employee> list()


### Example

```typescript
import {
    EmployeeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeControllerApi(configuration);

const { status, data } = await apiInstance.list();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Employee>**

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

# **saveEmployeeWithContract**
> SpecialContractDTO saveEmployeeWithContract()


### Example

```typescript
import {
    EmployeeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeControllerApi(configuration);

let matricule: string; // (default to undefined)
let nom: string; // (default to undefined)
let prenom: string; // (default to undefined)
let sexe: string; // (default to undefined)
let situationMatrimoniale: number; // (default to undefined)
let nationalite: number; // (default to undefined)
let dofbrid: string; // (default to undefined)
let typeContrat: 'STAFF_PDG' | 'DOZO' | 'STAGE' | 'AGENT_SECURITE'; // (default to undefined)
let dDeb: string; // (default to undefined)
let dFin: string; // (default to undefined)
let modepaiement: string; // (default to undefined)
let paiementNumber: string; // (default to undefined)
let netpayer: number; // (default to undefined)
let id: number; // (optional) (default to undefined)
let lieuHabitation: string; // (optional) (default to undefined)
let phoneNumber: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.saveEmployeeWithContract(
    matricule,
    nom,
    prenom,
    sexe,
    situationMatrimoniale,
    nationalite,
    dofbrid,
    typeContrat,
    dDeb,
    dFin,
    modepaiement,
    paiementNumber,
    netpayer,
    id,
    lieuHabitation,
    phoneNumber
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **matricule** | [**string**] |  | defaults to undefined|
| **nom** | [**string**] |  | defaults to undefined|
| **prenom** | [**string**] |  | defaults to undefined|
| **sexe** | [**string**] |  | defaults to undefined|
| **situationMatrimoniale** | [**number**] |  | defaults to undefined|
| **nationalite** | [**number**] |  | defaults to undefined|
| **dofbrid** | [**string**] |  | defaults to undefined|
| **typeContrat** | [**&#39;STAFF_PDG&#39; | &#39;DOZO&#39; | &#39;STAGE&#39; | &#39;AGENT_SECURITE&#39;**]**Array<&#39;STAFF_PDG&#39; &#124; &#39;DOZO&#39; &#124; &#39;STAGE&#39; &#124; &#39;AGENT_SECURITE&#39;>** |  | defaults to undefined|
| **dDeb** | [**string**] |  | defaults to undefined|
| **dFin** | [**string**] |  | defaults to undefined|
| **modepaiement** | [**string**] |  | defaults to undefined|
| **paiementNumber** | [**string**] |  | defaults to undefined|
| **netpayer** | [**number**] |  | defaults to undefined|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **lieuHabitation** | [**string**] |  | (optional) defaults to undefined|
| **phoneNumber** | [**string**] |  | (optional) defaults to undefined|


### Return type

**SpecialContractDTO**

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

