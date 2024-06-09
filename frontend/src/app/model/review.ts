export interface Review {
    id: string;
    authorId: string;
    bookingId: string;
    text: string;
    starts: number;
    state: string;
    imageUrlList: string[];
}