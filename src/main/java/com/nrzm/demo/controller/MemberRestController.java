package com.nrzm.demo.controller;

import com.nrzm.demo.entitiy.Member;
import com.nrzm.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 관리", description = "회원 목록조회, 조회, 입력, 수정, 삭제")
public class MemberRestController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 목록조회", description = "회원 목록조회 API")
    @ApiResponse(responseCode = "200", description = "처리 결과가 정상이면 id를 포함한 JSON 데이터를 응답", content = @Content(mediaType = "application/json"))
    public List<Member> getAllMembers() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 조회", description = "회원 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 결과가 정상이면 id를 포함한 JSON 데이터를 응답", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "회원ID를 찾을 수 없으면 404 Not Found", content = @Content(mediaType = "none"))
    })
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.findById(id);
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "회원 추가", description = "회원 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 결과가 정상이면 생성된 id를 포함한 JSON 데이터를 응답", content = @Content(mediaType = "application/json"))
    })
    public Member createMember(@RequestBody Member member) {
        return memberService.save(member);
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 수정", description = "회원 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 결과가 정상이면 200 Ok 응답"),
            @ApiResponse(responseCode = "404", description = "회원ID를 찾을 수 없으면 404 Not Found", content = @Content(mediaType = "none"))
    })
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member memberDetails) {
        Optional<Member> member = memberService.findById(id);
        if (member.isPresent()) {
            Member updatedMember = member.get();
            updatedMember.setMemberName(memberDetails.getMemberName());
            updatedMember.setMemberPassword(memberDetails.getMemberPassword());
            return ResponseEntity.ok(memberService.save(updatedMember));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "회원 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "처리 결과가 정상이면 204 No Content 응답", content = @Content(mediaType = "none"))
    })
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
