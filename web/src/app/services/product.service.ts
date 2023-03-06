import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { delay, Observable, Subject, tap } from 'rxjs';
import { Product } from '../interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  readonly URL: string = "http://localhost:8080/api/v1";

  files: File[] = [];

  products: { product: Product, link: string }[] = [];

  product: Product = {
    description: "",
    product: {
      type: "",
      brand: "",
      model: ""
    },
    location: 0
  }
  
  constructor(private http: HttpClient, private snackbar: MatSnackBar) { }

  openSnackBar(message: string, action: string) {
    this.snackbar.open(message, action, {
      duration: 5000,
    });
  }

  addProduct(): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.post(`${this.URL}/product`, this.product, { 'headers': headers });
  }

  getProducts(state: string,
    associativity: string,
    orderBy: string,
    limit?: number,
    brands?: string[],
    types?: string[],
    models?: string[],
    maxDistance?: number,
    afterId?: number,
    afterDistance?: number
  ): Observable<any> {
    console.log(associativity);

    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);

    let params = new HttpParams();
    params = params.append('state', state);
    params = params.append('associativity', associativity);
    params = params.append('orderBy', orderBy);

    return this.http.get(`${this.URL}/product`, { 'headers': headers, 'params': params });
  }

  getProduct(id: number): Observable<any> {
    console.log(id);

    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.get(`${this.URL}/product/${id}`, { 'headers': headers });
  }

  getTypes(): Observable<any> {
    return this.http.get(`${this.URL}/product/filters/types`);
  }

  getBrands(types: string): Observable<any> {
    let params = new HttpParams();
    params = params.append('types', types);
    return this.http.get(`${this.URL}/product/filters/brands`, { 'params': params });
  }

  getBrandsNo(): Observable<any> {
    // let params = new HttpParams();
    // params = params.append('types', types);
    return this.http.get(`${this.URL}/product/filters/brands`);
  }

  getModels(types: string, brands: string): Observable<any> {
    let params = new HttpParams();
    params = params.append('types', types);
    params = params.append('brands', brands);
    return this.http.get(`${this.URL}/product/filters/models`, { 'params': params })
  }

  getModelsNo(): Observable<any> {
    // let params = new HttpParams();
    // params = params.append('types', types);
    return this.http.get(`${this.URL}/product/filters/models`);
  }

  uploadProductImg(id: string, img: File, index: string): Observable<any> {
    console.log("IMG");

    console.log(img);

    const req = new FormData();
    req.append("pid", id);
    req.append("image", img);
    req.append("index", index);
    console.log(req);

    return this.http.post(`${this.URL}/image/ProductImage`, req, { responseType: 'text' });
  }

  getProductImages(pid: number): Observable<any> {
    return this.http.get(`${this.URL}/image/ProductImage/${pid}`);
  }

  setImgForProduct(products: Product[]) {
    for (let i = 0; i < products.length; i++) {
      const product = products[i];
      this.getProductImages(product.id!).subscribe((link) => {
        this.products.push({ product: product, link: link[0] });
      });
    }
  }

  setReservation(pid: number, appointment: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);

    let params = new HttpParams();
    params = params.append('appointment', appointment);

    return this.http.patch(`${this.URL}/product/${pid}/reserve`, null, { 'headers': headers, 'params': params });
  }

  undoReservation(pid: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.patch(`${this.URL}/product/${pid}/undoReservation`, null, { 'headers': headers });
  }
}
