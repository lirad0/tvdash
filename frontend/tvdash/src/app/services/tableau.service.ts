import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

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
    return this.http.get<TableauCard[]>(`${this.baseUrl}/cards`).pipe(
      map((cards) => cards.map((card) => ({ ...card, imageUrl: this.resolveImageUrl(card.imageUrl) }))),
    );
  }

  getUrlOnlyItems(): Observable<UrlOnlyItem[]> {
    return this.http.get<UrlOnlyItem[]>(`${this.baseUrl}/url-only-items`);
  }

  private resolveImageUrl(imageUrl: string | undefined): string {
    if (!imageUrl) {
      return '';
    }

    if (/^https?:\/\//i.test(imageUrl) || imageUrl.startsWith('data:') || imageUrl.startsWith('/')) {
      return imageUrl;
    }

    return `${API_BASE_URL}/images/${encodeURIComponent(imageUrl)}`;
  }
}
