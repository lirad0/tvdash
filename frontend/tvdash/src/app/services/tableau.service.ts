import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { TableauCard } from '../models/tableau-card';
import { UrlOnlyItem } from '../models/url-only-item';
import { API_BASE_URL } from './api-config';

@Injectable({
  providedIn: 'root',
})
export class TableauService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${API_BASE_URL}/tableau`;

  getCards(): Observable<TableauCard[]> {
    return this.http.get<TableauCard[]>(`${this.baseUrl}/cards`);
  }

  getUrlOnlyItems(): Observable<UrlOnlyItem[]> {
    return this.http.get<UrlOnlyItem[]>(`${this.baseUrl}/url-only-items`);
  }
}
