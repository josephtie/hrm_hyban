# ContratPersonnelControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletecontratpers**](#deletecontratpers) | **POST** /api/personnels/deletecontratpersonnel | |
|[**getChearchContract**](#getchearchcontract) | **POST** /api/personnels/cherchcontratpersonnelActif | |
|[**getContratListDepartJSON**](#getcontratlistdepartjson) | **POST** /api/personnels/listcontratpersonnelDepartjson | |
|[**getContratListExpiredDateJSON**](#getcontratlistexpireddatejson) | **POST** /api/personnels/listcontratpersonnelExpDatejson | |
|[**getContratListExpiredJSON**](#getcontratlistexpiredjson) | **POST** /api/personnels/listcontratpersonnelExpjson | |
|[**getContratListExpiredPeriodeJSON**](#getcontratlistexpiredperiodejson) | **POST** /api/personnels/listcontratpersonnelExpPeriodejson | |
|[**getContratListJSON**](#getcontratlistjson) | **POST** /api/personnels/listcontratpersonneljson | |
|[**getContratPersonnelListActifJSON**](#getcontratpersonnellistactifjson) | **POST** /api/personnels/listcontratpersonnelActifjson | |
|[**getContratPersonnelListJSON**](#getcontratpersonnellistjson) | **POST** /api/personnels/listcontratparpersonneljson | |
|[**getExpireContract**](#getexpirecontract) | **POST** /api/personnels/listexpirecontratpersonnel | |
|[**getExpireContractDelai**](#getexpirecontractdelai) | **POST** /api/personnels/listexpirecontratpersonnelDelai | |
|[**getExpireContractday15**](#getexpirecontractday15) | **POST** /api/personnels/listexpirecontratpersonneldays | |
|[**listContratParPersonnel**](#listcontratparpersonnel) | **POST** /api/personnels/listcontratparpersonnel | |
|[**saveContratpers**](#savecontratpers) | **POST** /api/personnels/savecontratpersonnel | |
|[**updateContratPersonnel**](#updatecontratpersonnel) | **POST** /api/personnels/endcontratpersonnel | |
|[**updateContratPersonnelsursal**](#updatecontratpersonnelsursal) | **POST** /api/personnels/updatecontratpersonnelSursal | |
|[**viewService1**](#viewservice1) | **GET** /api/personnels/contrat | |
|[**viewService2**](#viewservice2) | **HEAD** /api/personnels/contrat | |
|[**viewService3**](#viewservice3) | **POST** /api/personnels/contrat | |
|[**viewService4**](#viewservice4) | **PUT** /api/personnels/contrat | |
|[**viewService5**](#viewservice5) | **PATCH** /api/personnels/contrat | |
|[**viewService6**](#viewservice6) | **DELETE** /api/personnels/contrat | |
|[**viewService7**](#viewservice7) | **OPTIONS** /api/personnels/contrat | |

# **deletecontratpers**
> ContratPersonnelDTO deletecontratpers(idRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deletecontratpers(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getChearchContract**
> ContratPersonnelDTO getChearchContract(idRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.getChearchContract(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratListDepartJSON**
> ContratPersonnelDTO getContratListDepartJSON(paginationRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getContratListDepartJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratListExpiredDateJSON**
> ContratPersonnelDTO getContratListExpiredDateJSON(paginationIdRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationIdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationIdRequest: PaginationIdRequest; //

const { status, data } = await apiInstance.getContratListExpiredDateJSON(
    paginationIdRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationIdRequest** | **PaginationIdRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratListExpiredJSON**
> ContratPersonnelDTO getContratListExpiredJSON(paginationIdRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationIdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationIdRequest: PaginationIdRequest; //

const { status, data } = await apiInstance.getContratListExpiredJSON(
    paginationIdRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationIdRequest** | **PaginationIdRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratListExpiredPeriodeJSON**
> ContratPersonnelDTO getContratListExpiredPeriodeJSON(paginationIdRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationIdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationIdRequest: PaginationIdRequest; //

const { status, data } = await apiInstance.getContratListExpiredPeriodeJSON(
    paginationIdRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationIdRequest** | **PaginationIdRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratListJSON**
> ContratPersonnelDTO getContratListJSON(paginationRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getContratListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratPersonnelListActifJSON**
> ContratPersonnelDTO getContratPersonnelListActifJSON(paginationRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getContratPersonnelListActifJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getContratPersonnelListJSON**
> ContratPersonnelDTO getContratPersonnelListJSON(paginationIdRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    PaginationIdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let paginationIdRequest: PaginationIdRequest; //

const { status, data } = await apiInstance.getContratPersonnelListJSON(
    paginationIdRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationIdRequest** | **PaginationIdRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **getExpireContract**
> Array<ContratPersonnel> getExpireContract()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.getExpireContract();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ContratPersonnel>**

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

# **getExpireContractDelai**
> Array<ContratPersonnel> getExpireContractDelai(nbreRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    NbreRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let nbreRequest: NbreRequest; //

const { status, data } = await apiInstance.getExpireContractDelai(
    nbreRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **nbreRequest** | **NbreRequest**|  | |


### Return type

**Array<ContratPersonnel>**

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

# **getExpireContractday15**
> Array<ContratPersonnel> getExpireContractday15()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.getExpireContractday15();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ContratPersonnel>**

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

# **listContratParPersonnel**
> Array<ContratPersonnel> listContratParPersonnel(idRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.listContratParPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Array<ContratPersonnel>**

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

# **saveContratpers**
> ContratPersonnelDTO saveContratpers(contratPersonnelRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    ContratPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let contratPersonnelRequest: ContratPersonnelRequest; //

const { status, data } = await apiInstance.saveContratpers(
    contratPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **contratPersonnelRequest** | **ContratPersonnelRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **updateContratPersonnel**
> ContratPersonnelDTO updateContratPersonnel(endContractRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    EndContractRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let endContractRequest: EndContractRequest; //

const { status, data } = await apiInstance.updateContratPersonnel(
    endContractRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **endContractRequest** | **EndContractRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **updateContratPersonnelsursal**
> ContratPersonnelDTO updateContratPersonnelsursal(sursalaireRequest)


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration,
    SursalaireRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

let sursalaireRequest: SursalaireRequest; //

const { status, data } = await apiInstance.updateContratPersonnelsursal(
    sursalaireRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sursalaireRequest** | **SursalaireRequest**|  | |


### Return type

**ContratPersonnelDTO**

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

# **viewService1**
> string viewService1()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService1();
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

# **viewService2**
> string viewService2()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService2();
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

# **viewService3**
> string viewService3()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService3();
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

# **viewService4**
> string viewService4()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService4();
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

# **viewService5**
> string viewService5()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService5();
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

# **viewService6**
> string viewService6()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService6();
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

# **viewService7**
> string viewService7()


### Example

```typescript
import {
    ContratPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ContratPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewService7();
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

